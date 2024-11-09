package org.springframework.test;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class SimpleBeanContainerTest {

    @Test
    public void testGetBean() {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.registerBean("helloService", new HelloService());
        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
        AbstractObjectAssert<?, HelloService> notNull = assertThat(helloService).isNotNull();
        AbstractStringAssert<?> helllo = assertThat(helloService.sayHello()).isEqualTo("hello");


    }

    class HelloService{
        public String sayHello(){
            System.out.println("hello");
            return "hello";
        }
    }
}
