package com.xyzla.netty.springmvc.netty.http;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


public class MagicWebServer {

    private static Logger logger = LoggerFactory.getLogger(MagicWebServer.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:config/spring/spring-common.xml");
        Integer port = 6001;
        DispatcherServlet servlet = getDispatcherServlet(ctx);
        NettyHttpServer server = new NettyHttpServer(port, servlet);
        server.start();
    }

    public static DispatcherServlet getDispatcherServlet(ApplicationContext ctx) {

        XmlWebApplicationContext mvcContext = new XmlWebApplicationContext();
        mvcContext.setConfigLocation("classpath:dispatcher-servlet.xml");
        mvcContext.setParent(ctx);
        MockServletConfig servletConfig = new MockServletConfig(mvcContext.getServletContext(), "dispatcherServlet");
        DispatcherServlet dispatcherServlet = new DispatcherServlet(mvcContext);
        try {
            dispatcherServlet.init(servletConfig);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return dispatcherServlet;
    }
}