package com.gm.warn.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringContextUtils.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T)context.getBean(name);
    }

    public static <T> T getBean(Class<T> clz) throws BeansException {
        return (T)context.getBean(clz);
    }
}
