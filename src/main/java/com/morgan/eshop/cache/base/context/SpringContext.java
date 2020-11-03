package com.morgan.eshop.cache.base.context;

import org.springframework.context.ApplicationContext;

/**
 * @Description: Spring上下文对象
 * @Date:2020/9/13
 * @User:morgan.b.chen
 */
public class SpringContext {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContext.applicationContext = applicationContext;
    }
}
