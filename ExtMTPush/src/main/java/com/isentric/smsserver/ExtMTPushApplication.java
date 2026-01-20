package com.isentric.smsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * ExtMTPush Spring Boot Application
 * Main entry point for the SMS Gateway Service
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.isentric"})
public class ExtMTPushApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ExtMTPushApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ExtMTPushApplication.class);
    }
}

