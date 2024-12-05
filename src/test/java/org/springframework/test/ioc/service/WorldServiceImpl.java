package org.springframework.test.ioc.service;

public class WorldServiceImpl implements WorldService{

    private String name;
    @Override
    public void explore() {
        System.out.println("The earth is going to explore");
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
