package org.springframework.aop.framework;

import org.springframework.aop.AdvisedSupport;

public class ProxyFactory extends AdvisedSupport{
    private AdvisedSupport advisedSupport;

    public ProxyFactory(){}

    public ProxyFactory(AdvisedSupport advisedSupport){
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy(){
        return createProxy().getProxy();
    }

    private AopProxy createProxy(){
        if(this.isProxyTargetClass() || this.getTargetSource().getTargetClass().length == 0){
            return new CglibAopProxy(this);
        }
        return new JdkDynamicAopProxy(this);
    }

}
