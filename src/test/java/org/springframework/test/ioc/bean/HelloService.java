package org.springframework.test.ioc.bean;

public class HelloService {

    public String sayHello(){
        System.out.println("hello");
        return "hello";
    }

}
