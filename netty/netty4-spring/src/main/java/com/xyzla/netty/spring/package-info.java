/**
 * 该项目为SPRING MVC+NETTY
 * <p>
 * 启动方式有两种：
 * 1. 通过运行 com.xyzla.netty.spring.TestServer 中main方法 直接启动 netty；
 * 2. 以web项目方式 启动netty，有两种方式:
 * 2.1 通过配置 spring-common.xml,如 <bean id="myNettyServer" class="com.xyzla.netty.spring.server.ServerManager" init-method="startServer" destroy-method="stopServer"/>
 * 2.2 通过配置 web.xml,如 <listener><listener-class>com.xyzla.netty.spring.NettyListener</listener-class></listener>
 */
package com.xyzla.netty.spring;
