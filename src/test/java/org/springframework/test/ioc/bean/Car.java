package org.springframework.test.ioc.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.stereotype.Component;

@Component
public class Car {
    @Value("${brand}")
    private String brand;


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                '}';
    }

}
