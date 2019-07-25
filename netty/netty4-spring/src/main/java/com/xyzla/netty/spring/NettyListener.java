package com.xyzla.netty.spring;

import com.sun.crypto.provider.BlowfishKeyGenerator;
import com.xyzla.netty.spring.server.ServerManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class NettyListener implements ServletContextListener {

    ServerManager manager;

    public void contextInitialized(ServletContextEvent sce) {
        manager = new ServerManager();
        try {
            manager.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        try {
            this.manager.stopServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
