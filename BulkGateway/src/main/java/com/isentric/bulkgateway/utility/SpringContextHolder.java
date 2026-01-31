package com.isentric.bulkgateway.utility;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Simple holder for the Spring ApplicationContext so non-managed utilities can access beans.
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        CONTEXT = applicationContext;
    }

    public static ApplicationContext getContext() {
        return CONTEXT;
    }
}
