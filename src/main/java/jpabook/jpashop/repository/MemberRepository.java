package jpabook.jpashop.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;

import jpabook.jpashop.domain.Member;

@Repository
public class MemberRepository {

    private final EntityManager entityManager;

    public MemberRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Member member) {
        entityManager.persist(member);
    }

    public Member findOne(Long id) {
        return entityManager.find(Member.class, id);
    }

    public List<Member> findAll() {
        return entityManager.createQuery("select m from Member as m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return entityManager.createQuery("select m from Member as m where m.name=:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
