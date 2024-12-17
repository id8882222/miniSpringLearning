package org.springframework.test.ioc.ioc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.ioc.bean.Car;

import java.util.concurrent.TimeUnit;

public class lazeInitTest {
    @Test
    public void testLazyInit() throws InterruptedException{
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:lazy-test.xml");
        System.out.println(System.currentTimeMillis() + ":applicationContext-over");
        TimeUnit.SECONDS.sleep(1);
        Car c = (Car) applicationContext.getBean("car");
        c.showTime();
    }
}
