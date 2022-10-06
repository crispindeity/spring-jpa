package jpabook.jpashop.repository.order.query;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager entityManager;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return entityManager.createQuery(
                        "SELECT new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " FROM OrderItem oi" +
                                " JOIN oi.item i" +
                                " WHERE oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return entityManager.createQuery(
                        "SELECT new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " FROM Order o" +
                                " JOIN o.member m" +
                                " JOIN o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderItemQueryDto> orderItems = findOrderItemMap(toOrderIds(findOrders()));

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(groupingBy(OrderItemQueryDto::getOrderId));

        findOrders().forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return findOrders();
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(toList());
    }

    private List<OrderItemQueryDto> findOrderItemMap(List<Long> orderIds) {
        return entityManager.createQuery(
                        "SELECT new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " FROM OrderItem oi" +
                                " JOIN oi.item i" +
                                " WHERE oi.order.id IN :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return entityManager.createQuery(
                "SELECT new " +
                        " jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " FROM Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
