package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

public interface FactoryBean<T> {
    T getObject() throws BeansException;
    boolean isSingleton();
}
