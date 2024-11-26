package org.springframework.aop.framework;

import org.springframework.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    //用于保存代理配置信息
    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised){
        this.advised = advised;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(advised.getMethodMatcher().matches(method, advised.getTargetSource().getClass())){
            //代理方法
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectionMethodInvocation(advised.getTargetSource().getTarget(), method, args));
        }
        return method.invoke(advised.getTargetSource().getTarget(), args);
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
