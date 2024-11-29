package org.springframework.test.ioc.aop;




import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.*;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.CglibAopProxy;
import org.springframework.aop.framework.JdkDynamicAopProxy;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.MethodBeforeAdvisorInterceptor;
import org.springframework.test.ioc.common.WorldServiceBeforeAdvice;
import org.springframework.test.ioc.common.event.WorldServiceInterceptor;
import org.springframework.test.ioc.service.WorldService;
import org.springframework.test.ioc.service.WorldServiceImpl;
import org.springframework.test.ioc.service.WorldServiceWithExceptionImpl;

public class DynamicProxyTest {
    private AdvisedSupport advisedSupport;

    @BeforeEach
    public void setup(){
        WorldService worldService = new WorldServiceImpl();
        advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(worldService);
        WorldServiceInterceptor methodInterceptor = new WorldServiceInterceptor();
        MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* org.springframework.test.ioc.service.WorldService.explore(..))").getMethodMatcher();
        advisedSupport.setTargetSource(targetSource);
        advisedSupport.setMethodInterceptor(methodInterceptor);
        advisedSupport.setMethodMatcher(methodMatcher);
    }

    @Test
    public void testJdkDynamicProxy() throws Exception{
        WorldService proxy = (WorldService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        proxy.explore();
    }

    @Test
    public void testCglibDynamicProxy() throws Exception{
        WorldService proxy = (WorldService) new CglibAopProxy(advisedSupport).getProxy();
        proxy.explore();
    }

    @Test
    public void testProxyFactory(){
        //使用jdk动态代理
        advisedSupport.setProxyTargetClass(false);
        WorldService proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explore();
        //使用CGlib动态代理
        advisedSupport.setProxyTargetClass(true);
        proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explore();
    }

    @Test
    public void testBeforeAdvice() throws Exception{
        //设置BeforeAdvice
        WorldServiceBeforeAdvice beforeAdvice = new WorldServiceBeforeAdvice();
        GenericInterceptor methodInterceptor = new GenericInterceptor();
        methodInterceptor.setBeforeAdvice(beforeAdvice);
        advisedSupport.setMethodInterceptor(methodInterceptor);

        WorldService proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
        proxy.explore();
    }

    @Test
    public void testAdvisor() throws Exception{
        WorldService worldService = new WorldServiceImpl();

        //advisor是Pointcut和advice的结合
        String expression = "execution(* org.springframework.test.ioc.service.WorldService.explore(..))";
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(expression);
        MethodBeforeAdvisorInterceptor methodInterceptor = new MethodBeforeAdvisorInterceptor(new WorldServiceBeforeAdvice());
        advisor.setAdvice(methodInterceptor);

        ClassFilter classFilter = advisor.getPointcut().getClassFilter();
        if(classFilter.matches(worldService.getClass())){
            AdvisedSupport advisedSupport = new AdvisedSupport();

            TargetSource targetSource  = new TargetSource(worldService);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

            WorldService proxy = (WorldService) new ProxyFactory(advisedSupport).getProxy();
            proxy.explore();

        }

    }
}
