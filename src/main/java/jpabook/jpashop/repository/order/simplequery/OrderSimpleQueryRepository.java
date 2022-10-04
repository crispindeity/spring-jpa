package jpabook.jpashop.repository.order.simplequery;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager entityManager;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return entityManager.createQuery(
                "SELECT new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " FROM Order o" +
                        " JOIN o.member m" +
                        " JOIN o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
}
