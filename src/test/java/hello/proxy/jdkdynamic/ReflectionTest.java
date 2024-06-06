package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {
    @Slf4j
    public static class Hello{
        public String callA() {
            log.info("call A");
            return "A";
        }

        public String callB() {
            log.info("call B");
            return "B";
        }
    }

    @Test
    void reflection0() {
        Hello target = new Hello();
        //공통로직 1 실행
        log.info("start");
        String result = target.callA();     //호출하는 로직만 다름
        log.info("result = {}",result);
        //공통로직 1 종료

        //공통로직 2 실행
        log.info("start");
        String result2 = target.callB();
        log.info("result = {}",result2);
        //공통로직 2 종료
    }


    @Test
    void reflection1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();

        Method methodCallA = classHello.getMethod("callA");
        Object result1 = methodCallA.invoke(target);
        log.info("result = {}", result1);


        Method methodCallB = classHello.getMethod("callB");
        Object result2 = methodCallB.invoke(target);
        log.info("result = {}", result2);

    }


    @Test
    void reflection2() throws Exception {
        Class<?> classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
        Hello target = new Hello();

        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);

        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);

    }

    private void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
        //공통로직 1 실행
        log.info("start");
        Object result = method.invoke(target);  //reflection0의 로직을 추상화한 것
        log.info("result = {}",result);
        //공통로직 1 종료
    }
}
