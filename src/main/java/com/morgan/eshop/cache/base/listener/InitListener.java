package com.morgan.eshop.cache.base.listener;

import com.morgan.eshop.cache.base.context.SpringContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @Description: 初始化监听器
 * @Date:2020/9/13
 * @User:morgan.b.chen
 */
public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SpringContext.setApplicationContext(webApplicationContext);
//        new Thread(new KafkaConsumer("cache-message")).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
