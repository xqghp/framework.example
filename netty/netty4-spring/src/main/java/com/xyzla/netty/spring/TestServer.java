package com.xyzla.netty.spring;

import com.xyzla.netty.spring.context.AppContext;
import com.xyzla.netty.spring.server.ServerManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestServer {
    static {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:config/spring/spring-netty.xml");
        AppContext ac = new AppContext();
        ac.setApplicationContext(ctx);
    }

    public static void main(String[] args) throws Exception {
        ServerManager manager = new ServerManager();
        //manager.startServer(args[0]);
        manager.startServer();
    }
}
