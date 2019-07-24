package com.xyzla.netty.spring.server;

import com.xyzla.netty.spring.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class NettyTCPServer extends AbstractNettyServer {
    private static final Logger LOG = LoggerFactory.getLogger(NettyTCPServer.class);

    private ServerBootstrap serverBootstrap;

    public NettyTCPServer(NettyConfig nettyConfig, ChannelInitializer<? extends Channel> channelInitializer) {
        super(nettyConfig, channelInitializer);
    }

    @Override
    public void startServer() throws Exception {
        try {
            serverBootstrap = new ServerBootstrap();
            Map<ChannelOption<?>, Object> channelOptions = nettyConfig.getChannelOptions();
            if (null != channelOptions) {
                Set<ChannelOption<?>> keySet = channelOptions.keySet();
                // 获取configuration配置到channelOption
                for (ChannelOption option : keySet) {
                    serverBootstrap.option(option, channelOptions.get(option));
                }
            }
            // reactor多线程模型，配置bossGroup和workGroup
            // bossGroup和workGroup使用spring容器管理
            serverBootstrap.group(getBossGroup(), getWorkerGroup()).channel(NioServerSocketChannel.class).childHandler(getChannelInitializer());
            // 绑定端口，启动并监听
            Channel serverChannel = serverBootstrap.bind(nettyConfig.getSocketAddress()).sync().channel();
            ALL_CHANNELS.add(serverChannel);
        } catch (Exception e) {
            LOG.error("TCP Server start error {}, going to shut down", e);
            super.stopServer();
            throw e;
        }
    }

    @Override
    public TransmissionProtocol getTransmissionProtocol() {
        return TRANSMISSION_PROTOCOL.TCP;
    }

    // 配置自己的initializer
    @Override
    public void setChannelInitializer(ChannelInitializer<? extends Channel> initializer) {
        this.channelInitializer = initializer;
        serverBootstrap.childHandler(initializer);
    }

    @Override
    public String toString() {
        return "NettyTCPServer [socketAddress=" + nettyConfig.getSocketAddress() + ", portNumber=" + nettyConfig.getPortNumber() + "]";
    }
}
