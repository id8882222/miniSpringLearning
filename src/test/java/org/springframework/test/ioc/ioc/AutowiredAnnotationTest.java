package org.springframework.test.ioc.ioc;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.ioc.bean.Person;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AutowiredAnnotationTest {
    @Test
    public void testAutowiredAnnotation() throws Exception{
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:autowired-annotation.xml");

        Person person = applicationContext.getBean(Person.class);
        assertThat(person.getCar()).isNotNull();
    }
}
