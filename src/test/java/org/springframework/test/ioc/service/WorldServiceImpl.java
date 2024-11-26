package org.springframework.test.ioc.service;

public class WorldServiceImpl implements WorldService{

    @Override
    public void explore() {
        System.out.println("The earth is going to explore");
    }
}
