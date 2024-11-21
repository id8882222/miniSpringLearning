package org.springframework.test.ioc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.ioc.bean.Car;

import java.lang.annotation.Retention;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FactoryBeanTest {
    @Test
    public void testFactoryBean() throws BeansException{
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");
        Car car = applicationContext.getBean("car", Car.class);
        assertThat(car.getBrand()).isEqualTo("porsche");
    }
}
