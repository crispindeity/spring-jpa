package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpabookApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpabookApplication.class, args);
    }

    /**
     * InvalidDefinitionException 예외가 발생하는걸 수정하기 위해 추가
     * hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
     *  * LAZY 로딩을 강제 시키는 옵션, 모든 LAZY 로딩 등록 되어 있는걸 강제 시켜 성능상 문제가 매우 많다.
     * @return
     */
    @Bean
    Hibernate5Module hibernate5Module() {
        Hibernate5Module hibernate5Module = new Hibernate5Module();
//        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5Module;
    }
}
