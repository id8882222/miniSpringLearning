package org.springframework.beans.factory;

import org.springframework.beans.exceptions.BeansException;

/**
 * 实现接口，能够感知所属BeanFactory
 */
public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
