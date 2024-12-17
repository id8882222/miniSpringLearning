package org.springframework.aop.framework;

import org.springframework.aop.AdvisedSupport;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

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
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
//            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(advised.getTargetSource().getTarget(), method, args, proxy);
//            if(advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())){
//                return advised.getMethodInterceptor().invoke(methodInvocation);
//            }
//            return methodInvocation.proceed();
            //获取目标对象
            Object target = advised.getTargetSource().getTarget();
            Class<?> targetClass = target.getClass();
            Object retVal = null;
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
            CglibMethodInvocation methodInvocation = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy);
            if(chain == null || chain.isEmpty()){
                //代理方法
                retVal = methodProxy.invoke(target, args);
            }else{
                retVal = methodInvocation.proceed();
            }
            return retVal;
        }
    }

    private static class CglibMethodInvocation extends ReflectionMethodInvocation{
        private final MethodProxy methodProxy;
        public CglibMethodInvocation(Object proxy, Object target, Method method, Object[] arguments, Class<?> targetClass,
                                     List<Object> interceptorsAndDynamicMethodMatches,
                                     MethodProxy methodProxy){
            super(proxy, target, method, arguments, targetClass, interceptorsAndDynamicMethodMatches);
            this.methodProxy = methodProxy;
        }
        @Override
        public Object proceed() throws Throwable{
//            return this.methodProxy.invoke(this.target, this.arguments);
            return super.proceed();
        }
    }
}
