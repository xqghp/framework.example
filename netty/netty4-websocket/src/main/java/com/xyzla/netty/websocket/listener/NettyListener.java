package com.xyzla.netty.websocket.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import com.xyzla.netty.websocket.server.ChildChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.err.println("nettyListener Startup!");
        new Thread() {
            @Override
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workGroup);
                    b.channel(NioServerSocketChannel.class);
                    b.childHandler(new ChildChannelHandler());

                    System.out.println("服务端开启等待客户端连接 ... ...");

                    Channel ch = b.bind(7397).sync().channel();

                    ch.closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }
            }
        }.start();

        System.err.println("nettyListener end!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}