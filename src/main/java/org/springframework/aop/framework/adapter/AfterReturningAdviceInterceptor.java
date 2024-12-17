package org.springframework.aop.framework.adapter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterAdvice;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {

    private AfterReturningAdvice advice;

    public AfterReturningAdviceInterceptor(){}

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice){
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object retVal = invocation.proceed();
        this.advice.afterReturning(retVal, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
        return retVal;
    }

    @Override
    public void after(Method method, Object[] args, Object target) throws Throwable {

    }
}
