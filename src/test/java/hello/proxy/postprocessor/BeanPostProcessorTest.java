package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {
    @Test
    void basicConfig() {
        //spring container 에  아래의 BasicConfig class 를 넣어줌
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        //해당 매개변수의 클래스 중 해당 이름의 빈을 가져옴 postProcessor 바꿔치기한 B 클래스로 처리
        // beanA 이름으로 B 객체가 전달
        B beanA = applicationContext.getBean("beanA", B.class);
        beanA.helloB();

        //A 는 빈으로 등록되지 않는다.
//        B bean = applicationContext.getBean(B.class);
        Assertions.assertThrows(NoSuchBeanDefinitionException.class,()->applicationContext.getBean(A.class));
    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {
        @Bean(name = "beanA")
        public A a(){
            return new A();
        }

        @Bean
        public AToBPostProcessor helloPostProcessor() {
            return new AToBPostProcessor();
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
        public void helloB() {
            log.info("hello B");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("bean name = {} bean = {}", beanName, bean);
            //처리하는 bean 이 A 이면 B를 빈에 등록
            if (bean instanceof A) {
                return new B();
            }
            return bean;
        }
    }
}
