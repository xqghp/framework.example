package com.xyzla.netty.anxpp.calculator.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Client implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Client.class);

    static ClientHandler client = new ClientHandler();
    private volatile boolean isChannelPrepared_;
    private int reConnectCount_ = 0;

    public static void main(String[] args) throws Exception {
        new Thread(new Client()).start();
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while (client.sendMsg(scanner.nextLine())) ;
    }

    @Override
    public void run() {
        connect();
    }

    private void connect() {
        final String host = "127.0.0.1";
        final int port = 9090;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(client);
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        reConnectCount_ = 0;
                        isChannelPrepared_ = true;
//                        startupListener_.onCompletion(true);
                        logger.info("与服务器{}:{}连接建立成功...", host, port);
                    } else {
                        isChannelPrepared_ = false;
//                        startupListener_.onCompletion(false);
                        logger.info("与服务器{}:{}连接建立失败...", host, port);
                    }
                }
            });
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            isChannelPrepared_ = false;
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();

            isChannelPrepared_ = false;
            reConnect(host, port);
        }
    }


    // 断线重连
    private void reConnect(String host, int port) {
        // fixme: 重连显式退出?
        try {
            isChannelPrepared_ = false;
            int delay = ++reConnectCount_ * 3;
            reConnectCount_ = reConnectCount_ > 23 ? 23 : reConnectCount_;
            logger.error("与服务器{}:{}连接已断开,重连次数{}, {}秒后重连...", host, port, reConnectCount_, delay);

            Thread.sleep(delay * 1000);
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}