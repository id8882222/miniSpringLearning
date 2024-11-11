package org.springframework.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;


public class BeanDefinitionAndBeanDefinitionRegistryTest {



    @Test
    public void testBeanFactory() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);
        beanFactory.registerBeanDefinition("helloService", beanDefinition);
        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        helloService.sayHello();

//        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        BeanDefinition beanDefinition = new BeanDefinition(InnerClass.class);
//        beanFactory.registerBeanDefinition("innerClass", beanDefinition);
//        InnerClass innerClass = (InnerClass) beanFactory.getBean("innerClass");
//        innerClass.sayHelloWorld();
    }

    public static class InnerClass{
        public InnerClass(){};
        public void sayHelloWorld(){
            System.out.println("hello world");
        }
    }
}
