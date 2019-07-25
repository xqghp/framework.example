package com.xyzla.netty.spring;

import com.xyzla.netty.spring.server.ServerManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 必须重开一个线程运行 netty,否则将会阻塞tomcat 主线程.
 */
public class NettyListener implements ServletContextListener {

    ServerManager manager;

    public void contextInitialized(ServletContextEvent sce) {
        new Thread() {
            @Override
            public void run() {
                manager = new ServerManager();
                try {
                    manager.startServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            this.manager.stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
