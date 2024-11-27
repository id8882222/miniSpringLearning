package org.springframework.aop.framework;

import org.springframework.aop.AdvisedSupport;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibAopProxy implements AopProxy{

    private final AdvisedSupport advised;

    public CglibAopProxy(AdvisedSupport advised){
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(advised.getTargetSource().getTarget().getClass());
        enhancer.setInterfaces(advised.getTargetSource().getTargetClass());
        enhancer.setCallback(new DynanicAdvisedInterceptor(advised));
        return enhancer.create();
    }

    private static class DynanicAdvisedInterceptor implements MethodInterceptor {
        private final AdvisedSupport advised;

        private DynanicAdvisedInterceptor(AdvisedSupport advised){
            this.advised = advised;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, args, proxy);
            if(advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())){
                return advised.getMethodInterceptor().invoke(methodInvocation);
            }
            return methodInvocation.proceed();
        }
    }

    private static class CglibMethodInvocation extends ReflectionMethodInvocation{
        private final MethodProxy methodProxy;
        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy){
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }
        @Override
        public Object proceed() throws Throwable{
            return this.methodProxy.invoke(this.target, this.arguments);
        }
    }
}
