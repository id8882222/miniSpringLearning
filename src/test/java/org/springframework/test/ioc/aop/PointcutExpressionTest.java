package org.springframework.test.ioc.aop;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.beans.exceptions.BeansException;
import org.springframework.test.ioc.bean.HelloService;

import java.lang.reflect.Method;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PointcutExpressionTest {
    @Test
    public void testPointcutExpression() throws BeansException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.springframework.test.ioc.bean.HelloService.*(..))");
        Class<HelloService> clazz = HelloService.class;
        Method method = clazz.getDeclaredMethod("sayHello");

        assertThat(pointcut.matches(clazz)).isTrue();
        assertThat(pointcut.matches(method, clazz)).isTrue();
    }
}
