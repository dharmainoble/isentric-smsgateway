package com.isentric.smsserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Resource configuration to ensure proper static resource and API path handling
 * This explicitly maps static resources while allowing REST endpoints to be routed properly
 */
@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Explicitly enable and configure static resource handlers
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/css/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/images/")
                .setCachePeriod(3600);

        // Handle favicon
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/favicon.ico")
                .setCachePeriod(86400);

        // Handle webjars for Bootstrap, jQuery, etc.
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(3600);

        // All other paths that don't match REST endpoints will return 404 via custom error controller
    }
}

