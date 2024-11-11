package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 注册表接口
 */
public interface BeanDefinitionRegistry {
    /**
     * 向注册表中注入BeanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
