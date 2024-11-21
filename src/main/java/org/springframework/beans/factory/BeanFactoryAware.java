package org.springframework.beans.factory;

import org.springframework.beans.exceptions.BeansException;

public interface BeanFactoryAware extends Aware{
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
