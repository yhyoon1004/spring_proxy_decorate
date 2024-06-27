package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppConfigV1;
import hello.proxy.config.AppConfigV2;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppConfigV1.class, AppConfigV2.class})
public class AutoProxyConfig {

//    @Bean     모든 객체에 아래 매핑해준 이름이 들어간 객체를 프록시 생성 문제 발생
    public Advisor advisor1(LogTrace logTrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*","order*", "save*");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,advice);
    }

//    @Bean
//    public Advisor advisor2(LogTrace logTrace) {
//        //pointcut
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
//                            // * -> 모든 반환타입 , hello.~ -> 포인트컷을 적용할 경로    ||(..)-> 모든 파라미터 상관없음
//        //advice
//        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
//        return new DefaultPointcutAdvisor(pointcut,advice);
//    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        //pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");//제외 조건 추가
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut,advice);
    }

}
