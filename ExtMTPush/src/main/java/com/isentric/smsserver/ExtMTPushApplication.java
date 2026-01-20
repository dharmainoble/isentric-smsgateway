package com.isentric.smsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.isentric"})
public class ExtMTPushApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExtMTPushApplication.class, args);
    }
}

