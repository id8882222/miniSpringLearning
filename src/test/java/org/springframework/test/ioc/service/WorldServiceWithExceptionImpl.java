package org.springframework.test.ioc.service;

public class WorldServiceWithExceptionImpl implements WorldService{

    @Override
    public void explore() {
        System.out.println("The Earth is going to explode with an Exception");
        throw new RuntimeException();
    }
}
