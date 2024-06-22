package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicTest {
    @Test
    void basicConfig() {
        //spring container 에  아래의 BasicConfig class 를 넣어줌
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BasicConfig.class);
        //해당 매개변수의 클래스 중 해당 이름의 빈을 가져옴
        A beanA = applicationContext.getBean("beanA", A.class);
        beanA.helloA();

        //B는 빈으로 등록되지 않는다.
//        B bean = applicationContext.getBean(B.class);
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,()->applicationContext.getBean(B.class));
    }

    @Slf4j
    @Configuration
    static class BasicConfig {
        @Bean(name = "beanA")
        public A a(){
            return new A();
        }
    }


    @Slf4j
    static class A{
        public void helloA() {
            log.info("hello A");
        }
    }
    @Slf4j
    static class B{
        public void helloA() {
            log.info("hello B");
        }
    }
}
