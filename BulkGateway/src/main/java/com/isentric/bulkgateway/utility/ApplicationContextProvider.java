package com.isentric.bulkgateway.utility;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (context == null) return null;
        return context.getBean(clazz);
    }

    public static ApplicationContextProvider getInstance() {
        // return provider wrapper if context initialized else null
        if (context == null) return null;
        ApplicationContextProvider provider = new ApplicationContextProvider();
        return provider;
    }
}

