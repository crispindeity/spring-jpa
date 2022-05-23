package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("상품주문")
    void orderTest() {
        //given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = createOrder(member, book, orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertSoftly(assertions -> {
            assertions.assertThat(OrderStatus.ORDER).as("상품 주문시 상태는 ORDER").isEqualTo(getOrder.getStatus());
            assertions.assertThat(getOrder.getOrderItems().size()).as("주문한 상품 종류 수가 정확해야 한다.").isEqualTo(1);
            assertions.assertThat(getOrder.getTotalPrice()).as("주문 가격은 가격 * 수량").isEqualTo(10000 * orderCount);
            assertions.assertThat(book.getStockQuantity()).as("주문 수량만큼 재고가 줄어야 한다.").isEqualTo(8);
        });
    }

    @Test
    @DisplayName("주문취소")
    void orderCancelTest() {
        //given
        Member member = createMember();
        Book item = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertSoftly(assertions -> {
            assertions.assertThat(getOrder.getStatus()).as("주문 취소시 상태는 CANCEL 이다.").isEqualTo(OrderStatus.CANCEL);
            assertions.assertThat(item.getStockQuantity()).as("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.").isEqualTo(10);
        });
    }

    @Test
    @DisplayName("상품주문_재고수량초과")
    void orderOverQuantityExceptionTest() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;
        //when

        //then
        assertThatThrownBy(() ->
                createOrder(member, item, orderCount)
        )
                .as("재고 수량 부족 예외가 발생해야 한다.")
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("need more stock");

    }

    private Long createOrder(Member member, Item item, int orderCount) {
        return orderService.order(member.getId(), item.getId(), orderCount);
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        entityManager.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        entityManager.persist(member);
        return member;
    }
}
