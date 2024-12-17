package org.springframework.test.ioc.aop;

import org.junit.jupiter.api.Test;
import org.springframework.aop.TargetSource;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.MethodBeforeAdvisorInterceptor;
import org.springframework.test.ioc.bean.A;
import org.springframework.test.ioc.common.WorldServiceAfterReturningAdvice;
import org.springframework.test.ioc.common.WorldServiceBeforeAdvice;
import org.springframework.test.ioc.service.WorldService;
import org.springframework.test.ioc.service.WorldServiceImpl;

public class proxyFactoryTest {
    @Test
    public void testAdvisor() throws Exception{
        WorldService worldService = new WorldServiceImpl();
        //Advisors是Pointcut和Advice的组合
        String expression = "execution(* org.springframework.test.ioc.service.WorldService.explore(..))";
        //第一个切面
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(expression);
        MethodBeforeAdvisorInterceptor methodInterceptor = new MethodBeforeAdvisorInterceptor(new WorldServiceBeforeAdvice());
        advisor.setAdvice(methodInterceptor);
        //第二个切面
        AspectJExpressionPointcutAdvisor advisor1 = new AspectJExpressionPointcutAdvisor();
        advisor1.setExpression(expression);
        AfterReturningAdviceInterceptor afterReturningAdviceInterceptor = new AfterReturningAdviceInterceptor(new WorldServiceAfterReturningAdvice());
        advisor1.setAdvice(afterReturningAdviceInterceptor);
        //通过ProxyFactory来获得代理
        ProxyFactory factory = new ProxyFactory();
        TargetSource targetSource = new TargetSource(worldService);
        factory.setTargetSource(targetSource);
        factory.setProxyTargetClass(true);
        factory.addAdvisor(advisor);
        factory.addAdvisor(advisor1);
        WorldService proxy = (WorldService) factory.getProxy();
        proxy.explore();
    }
}
