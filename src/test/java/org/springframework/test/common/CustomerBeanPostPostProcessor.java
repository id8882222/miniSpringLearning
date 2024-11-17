package org.springframework.test.common;

import org.springframework.beans.exceptions.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.test.bean.Car;

public class CustomerBeanPostPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("CustomerBeanPostProcessor#postProcessBeforeInitialization");
        if("car".equals(beanName)){
            ((Car) bean).setBrand("lamborghini");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("CustomerBeanPostProcessor#postProcessAfterInitialization");
        return bean;
    }
}
