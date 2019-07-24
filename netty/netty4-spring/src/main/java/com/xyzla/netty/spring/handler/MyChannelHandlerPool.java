package com.xyzla.netty.spring.handler;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里讲ChannelGroup单独放到一个类里，并有多个客户端使用
 * 同时ChannelGroup是static的
 * 说明：这不是唯一的处理方式
 */
public class MyChannelHandlerPool {
    private static Logger logger = LoggerFactory.getLogger(MyChannelHandlerPool.class);

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static List<Channel> channelList = new ArrayList<Channel>();

    public static void addChannel(Channel ch) {
        channelList.add(ch);
        logger.info("新增客户端 {},当前客户端数：{}", ch.id(), channelList.size());
    }

    public static void removeChannel(Channel ch) {
        channelList.remove(ch);
        logger.info("断开连接-客户端{}，当前客户端数：{}", ch.id(), channelList.size());
    }


}