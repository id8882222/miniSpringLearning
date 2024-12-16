package org.springframework.aop.framework.autoproxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.*;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.exceptions.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
    private DefaultListableBeanFactory beanFactory;

    private Set<Object> earlyProxyReferences = new HashSet<>();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       if(!earlyProxyReferences.contains(beanName)){
           return wrapIfNecessary(bean, beanName);
       }
       return bean;
        //避免死循环
//        if(isInfrastructureClass(bean.getClass())){
//            return null;
//        }
//        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
//        try{
//            for(AspectJExpressionPointcutAdvisor advisor : advisors){
//                ClassFilter classFilter = advisor.getPointcut().getClassFilter();
//                if(classFilter.matches(bean.getClass())){
//                    AdvisedSupport advisedSupport = new AdvisedSupport();
//
//                    TargetSource targetSource = new TargetSource(bean);
//                    advisedSupport.setTargetSource(targetSource);
//                    advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
//                    advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
//
//                    return new ProxyFactory(advisedSupport).getProxy();
//                }
//            }
//        }catch (Exception ex){
//            throw new BeansException("Error create proxy bean for: " + beanName, ex);
//        }
//        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException{
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    protected Object wrapIfNecessary(Object bean, String beanName){
        //避免死循环
        if(isInfrastructureClass(bean.getClass())){
            return bean;
        }
        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        try {
            for(AspectJExpressionPointcutAdvisor advisor : advisors){
                ClassFilter classFilter = advisor.getPointcut().getClassFilter();
                if(classFilter.matches(bean.getClass())){
                    AdvisedSupport advisedSupport = new AdvisedSupport();
                    TargetSource targetSource = new TargetSource(bean);
                    //改成cglib动态代理，因为代理类A是具体的实现类，而不是接口。
                    advisedSupport.setProxyTargetClass(true);
                    advisedSupport.setTargetSource(targetSource);
                    advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
                    advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

                    //返回代理对象
                    return new ProxyFactory(advisedSupport).getProxy();
                }
            }
        }catch (Exception ex){
            throw new BeansException("Error create proxy bean for: " + beanName, ex);
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
//        //避免死循环
//        if(isInfrastructureClass(beanClass)){
//            return null;
//        }
//        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
//        try{
//            for(AspectJExpressionPointcutAdvisor advisor : advisors){
//                ClassFilter classFilter = advisor.getPointcut().getClassFilter();
//                if(classFilter.matches(beanClass)){
//                    AdvisedSupport advisedSupport = new AdvisedSupport();
//
//                    BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
//                    Object bean =  beanFactory.getInstantiationStrategy().instantiate(beanDefinition);
//                    TargetSource targetSource = new TargetSource(bean);
//                    advisedSupport.setTargetSource(targetSource);
//                    advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
//                    advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
//
//                    return new ProxyFactory(advisedSupport).getProxy();
//                }
//            }
//        }catch (Exception ex){
//            throw new BeansException("Error create proxy bean for: " + beanName, ex);
//        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> beanClass){
        //检查传过来的bean是否为Advice、Pointcut、Advisor的子类
        boolean result = Advice.class.isAssignableFrom(beanClass)
                || Pointcut.class.isAssignableFrom(beanClass)
                || Advisor.class.isAssignableFrom(beanClass);
        return  result;
    }
}
