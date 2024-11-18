package org.springframework.beans.factory;

import org.springframework.beans.exceptions.BeansException;

import java.util.Map;

/**
 * 对BeanMap中所有bean对象的操作
 */
public interface ListableBeanFactory extends BeanFactory{
    /**
     * 返回指定类型的所有实例
     * @param type
     * @return
     * @param <T>
     * @throws BeansException
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回定义的所有bean的名称
     * @return
     */
    String[] getBeanDefinitionNames();
}
