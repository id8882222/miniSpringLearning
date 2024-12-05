package org.springframework.test.ioc.aop;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.ioc.service.WorldService;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class AutoProxyTest {
    @Test
    public void testAutoProxy() throws Exception{
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:auto-proxy.xml");

        WorldService worldService = applicationContext.getBean("worldService", WorldService.class);
        worldService.explore();
    }

    @Test
    public void testPopulateProxyBeanWithPropertyValues() throws Exception{
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:populate-proxy-bean-with-property-values.xml");

        WorldService worldService = applicationContext.getBean("worldService", WorldService.class);
        worldService.explore();
        assertThat(worldService.getName()).isEqualTo("earth");
    }
}
