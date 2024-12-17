package org.springframework.test.ioc.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class Car {

    private int price;

    private LocalDate produceDate;

    @Value("${brand}")
    private String brand;

    private long date;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getProduceDate() {
        return produceDate;
    }

    public void setProduceDate(LocalDate produceDate) {
        this.produceDate = produceDate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Car{" +
                "price=" + price +
                ", produceDate=" + produceDate +
                ", brand='" + brand + '\'' +
                '}';
    }

    public void init(){
        date = System.currentTimeMillis();
    }

    public void showTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        System.out.println(date + ":bean create");
    }

}
