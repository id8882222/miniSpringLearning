package org.springframework.test.ioc.common;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class WorldServiceAfterReturningAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("AfterReturningAdvice: do something after the earth explodes return");
    }
}
