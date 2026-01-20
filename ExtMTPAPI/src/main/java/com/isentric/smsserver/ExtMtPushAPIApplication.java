package com.isentric.smsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
    JmsAutoConfiguration.class,
    ArtemisAutoConfiguration.class
})
@EnableCaching
@EnableScheduling
public class ExtMtPushAPIApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ExtMtPushAPIApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ExtMtPushAPIApplication.class, args);
    }
}

