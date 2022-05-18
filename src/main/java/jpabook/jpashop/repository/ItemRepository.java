package jpabook.jpashop.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;

import jpabook.jpashop.domain.item.Item;

@Repository
public class ItemRepository {

    private final EntityManager entityManager;

    public ItemRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Item item) {
        if (item.getId() == null) {
            entityManager.persist(item);
        } else {
            entityManager.merge(item);
        }
    }

    public Item findOne(Long id) {
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll() {
        return entityManager.createQuery("select i from Item as i", Item.class)
                .getResultList();
    }
}
