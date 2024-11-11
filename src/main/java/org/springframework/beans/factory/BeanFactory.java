package org.springframework.beans.factory;

import org.springframework.beans.exceptions.BeansException;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;
}
