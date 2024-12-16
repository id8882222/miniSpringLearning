package org.springframework.beans.factory.config;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.exceptions.BeansException;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{
    /**
     * 在bean实例化之前执行
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

    /**
     * 提前暴露bean
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    default Object getEarlyBeanReference(Object bean, String beanName) throws BeansException{
        return bean;
    }
}
