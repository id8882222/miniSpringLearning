package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AdvisedSupport;
import org.springframework.cglib.proxy.MethodInterceptor;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    //用于保存代理配置信息
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised){
        this.advised = advised;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        if(advised.getMethodMatcher().matches(method, advised.getTargetSource().getClass())){
//            //代理方法
//            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
//            return methodInterceptor.invoke(new ReflectionMethodInvocation(advised.getTargetSource().getTarget(), method, args));
//        }
//        return method.invoke(advised.getTargetSource().getTarget(), args);
        //获取目标对象
        Object target = advised.getTargetSource().getTarget();
        Class<?> targetClass = target.getClass();
        Object retVal = null;
        //获取拦截器链
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        if(chain == null || chain.isEmpty()){
            return method.invoke(target, args);
        }else{
            //将拦截器链统一封装成ReflectMethodInvocation
            MethodInvocation invocation = new ReflectionMethodInvocation(proxy, target, method, args, targetClass, chain);
            //执行拦截器链
            retVal = invocation.proceed();
        }
        return retVal;

    }

    /**
     * param1:类加载器
     * param2：代理接口数组
     * param3：调用处理程序
     * @return
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getTargetClass(), this);
    }
}
