package com.isentric.bulkgateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@SpringBootApplication
@EntityScan(basePackages = "com.isentric.bulkgateway")
public class BulkGatewayApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BulkGatewayApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(BulkGatewayApplication.class, args);
        System.out.println("BulkGatewayWar Application Started Successfully!");
    }
}
