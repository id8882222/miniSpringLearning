package org.springframework.beans.factory.support;


import org.springframework.beans.exceptions.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private Map<String, Object> singletonObjects = new HashMap<>();

    protected Map<String, Object> earlySingletonObjects = new HashMap<>();

    private Map<String, ObjectFactory> singletonFactories = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if(singletonObject == null){
            singletonObject = earlySingletonObjects.get(beanName);
            if(singletonObject == null){
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if(singletonFactory != null){
                    singletonObject = singletonFactory.getObject();
                    //从三级缓存放入二级缓存
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    public void addSingleton(String beanName, Object singletonObject){
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory){
        singletonFactories.put(beanName, singletonFactory);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean){
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons(){
        ArrayList<String> beanNames = new ArrayList<>(disposableBeans.keySet());
        for(String beanName : beanNames){
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try{
                disposableBean.destroy();
            }catch (Exception e){
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
