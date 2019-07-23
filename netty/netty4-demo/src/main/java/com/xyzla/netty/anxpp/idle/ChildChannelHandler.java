package com.xyzla.netty.anxpp.idle;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;

public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
 
    @Override
    protected void initChannel(SocketChannel e) throws Exception {
 
        System.out.println("报告");
        System.out.println("信息：有一客户端链接到本服务端");
        System.out.println("IP:" + e.localAddress().getHostName());
        System.out.println("Port:" + e.localAddress().getPort());
        System.out.println("报告完毕");
 
        /**
         * 心跳包
         * 1、readerIdleTimeSeconds 读超时时间
         * 2、writerIdleTimeSeconds 写超时时间
         * 3、allIdleTimeSeconds 读写超时时间
         * 4、TimeUnit.SECONDS 秒[默认为秒，可以指定]
         */
        e.pipeline().addLast(new IdleStateHandler(2, 2, 2));
        // 基于换行符号解码器
        e.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 解码转String
        e.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        // 编码器 String
        e.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        // 处理心跳 【放在编码解码的下面，因为这个是通道有处理顺序】
        e.pipeline().addLast(new MyIdleHandler());
        // 在管道中添加我们自己的接收数据实现方法
        e.pipeline().addLast(new MyServerHanlder());
 
    }
 
}