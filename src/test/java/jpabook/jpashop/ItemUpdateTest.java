package jpabook.jpashop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

import jpabook.jpashop.domain.item.Book;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("업데이트 테스트")
    void test() throws Exception {
        Book book = entityManager.find(Book.class, 1L);

        //변경 감지 == dirty checking
        book.setName("아무이름");
    }
}
