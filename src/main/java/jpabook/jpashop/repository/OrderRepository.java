package jpabook.jpashop.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

import jpabook.jpashop.domain.Order;

@Repository
public class OrderRepository {

    private final EntityManager entityManager;

    public OrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Order order) {
        entityManager.persist(order);
    }

    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

//    public List<Order> findAll(OrderSearch orderSearch) {
//
//    }
}
