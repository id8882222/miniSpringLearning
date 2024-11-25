package org.springframework.test.ioc.ioc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.exceptions.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.test.ioc.bean.Car;
import org.springframework.test.ioc.bean.Person;
import org.springframework.test.ioc.common.CustomBeanFactoryPostProcessor;
import org.springframework.test.ioc.common.CustomerBeanPostPostProcessor;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BeanFactoryPostProcessorAndBeanPostProcessorTest {
    @Test
    public void testBeanFactoryPostProcessor() throws BeansException{
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //在所有beanDefinition加载完之后，在bean实例化之前，修改beanDefinition的属性值
        CustomBeanFactoryPostProcessor beanPostPostProcessor = new CustomBeanFactoryPostProcessor();
        beanPostPostProcessor.postProcessBeanFactory(beanFactory);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        //name属性在CustomBeanFactoryPostProcessor中被修改为ivy
        assertThat(person.getName()).isEqualTo("ivy");
    }

    @Test
    public void testBeanPostProcessor() throws Exception{
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        //添加bean实例化后的处理器
        CustomerBeanPostPostProcessor customerBeanPostPostProcessor = new CustomerBeanPostPostProcessor();
        beanFactory.addBeanPostProcessor(customerBeanPostPostProcessor);

        Car car = (Car) beanFactory.getBean("car");
        System.out.println(car);
        //brand属性在CustomerBeanPostProcessor中被修改为lamborghini
        assertThat(car.getBrand()).isEqualTo("lamborghini");
    }
}
