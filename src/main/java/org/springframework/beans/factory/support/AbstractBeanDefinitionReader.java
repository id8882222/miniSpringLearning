package org.springframework.beans.factory.support;

import org.springframework.beans.exceptions.BeansException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{
    private final BeanDefinitionRegistry registry;
    private ResourceLoader resourceLoader;
    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry){
        this(registry, new DefaultResourceLoader());
    }
    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader){
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }
    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }


    @Override
    public void loadBeanDefinitions(String[] loacations) throws BeansException {
        for(String location : loacations){
            loadBeanDefinitions(location);
        }
    }
}
