package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl(); //|-> 인터페이스 존재
        ProxyFactory proxyFactory = new ProxyFactory(target);// 실제 객체를 넣어 프록시 생성
        proxyFactory.addAdvice(new TimeAdvice());// 프록시 팩토리를 통해서 만든 프록시가 사용할 부가기능 로직을 설정
        // |-프록시가 제공하는 부가기능을 Advice 라고 함
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();//객체 동작 확인

        //프록시 객체인지 확인가능 (ProxyFactory 로 만든 것만 확인 가능)
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isFalse();

        //ProxyFactory 로 생성시 해당 객체의 | 인터페이스가 있다 -> JDK 동적 프록시 | 구체 클래스만 있다 -> CGLIB 를 통해 동적 프록시 생성

    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService(); //|-> 구체 클래스 존재
        ProxyFactory proxyFactory = new ProxyFactory(target);// 실제 객체를 넣어 프록시 생성
        proxyFactory.addAdvice(new TimeAdvice());// 프록시 팩토리를 통해서 만든 프록시가 사용할 부가기능 로직을 설정
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();//객체 동작 확인

        //프록시 객체인지 확인가능 (ProxyFactory 로 만든 것만 확인 가능)
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();

    }


    @Test
    @DisplayName("proxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl(); //|-> 인터페이스 존재
        ProxyFactory proxyFactory = new ProxyFactory(target);// 실제 객체를 넣어 프록시 생성
        proxyFactory.setProxyTargetClass(true);
        proxyFactory.addAdvice(new TimeAdvice());// 프록시 팩토리를 통해서 만든 프록시가 사용할 부가기능 로직을 설정
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();//객체 동작 확인

        //프록시 객체인지 확인가능 (ProxyFactory 로 만든 것만 확인 가능)
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
