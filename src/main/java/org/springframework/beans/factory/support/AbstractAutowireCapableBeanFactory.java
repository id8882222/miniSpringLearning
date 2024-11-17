package org.springframework.beans.factory.support;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.exceptions.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.context.annotation.Bean;
import cn.hutool.core.bean.BeanUtil;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException{
        return doCreateBean(beanName, beanDefinition);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition){

        Object bean = null;
        try {
            bean = createBeanInstance(beanDefinition);
            //为bean填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            bean = initializeBean(beanName, bean, beanDefinition);
        }catch (Exception e){
            throw new BeansException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
        return bean;
    }

    /**
     * 实例化bean
     * @param beanDefinition
     * @return
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition){
        return getInstantiationStrategy().instantiate(beanDefinition);
    }

    public InstantiationStrategy getInstantiationStrategy(){
        return instantiationStrategy;
    }


    /**
     * 为bean填充属性
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition){
        try {
            for(PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertiesValues()){
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                if(value instanceof BeanReference){
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
                BeanUtil.setFieldValue(bean, name, value);
            }
        }catch (Exception ex){
            throw new BeansException("Error setting property values for bean: " + beanName, ex);
        }
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException{
        Object result = existingBean;
        for(BeanPostProcessor processor : getBeanPostProcessors()){
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException{
        Object result = existingBean;
        for(BeanPostProcessor processor : getBeanPostProcessors()){
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if(current == null){
                return result;
            }
            result = current;
        }
        return result;
    }

    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition){
        //执行beanPostProcessor的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        // TODO: 2024/11/16 后面会在此处执行bean的初始化方法
        invokeInitMethods(beanName, wrappedBean, beanDefinition);
        //执行beanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }

    /**
     * 执行bean的初始化方法
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition){
        //TODO 后面会实现
        System.out.println("执行bean[" + beanName + "]的初始化方法");
    }
}
