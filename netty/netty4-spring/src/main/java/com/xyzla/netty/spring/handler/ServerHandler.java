package com.xyzla.netty.spring.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;
import java.util.Date;


/**
 * Handles a server-side channel.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {


    /*
     * channelAction
     * channel 通道
     * action  活跃的
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress().toString() + " channelActive");
        //添加到channelGroup 通道组
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
        MyChannelHandlerPool.addChannel(ctx.channel());

        //通知您已经链接上客户端
        String str = "您已经开启与服务端链接" + " " + ctx.channel().id() + new Date() + " " + ctx.channel().localAddress();
        ctx.writeAndFlush(str);
    }

    /*
     * channelInactive
     * channel 	通道
     * Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 从channelGroup中移除，当有客户端退出后，移除channel。
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
        MyChannelHandlerPool.removeChannel(ctx.channel());

        System.out.println(ctx.channel().localAddress().toString() + " channelInactive");
    }

    /**
     * 收到客户端消息
     *
     * @throws UnsupportedEncodingException
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf in = (ByteBuf) msg;
        byte[] req = new byte[in.readableBytes()];
        in.readBytes(req);
        String body = new String(req, "utf-8");
        System.out.println("收到客户端消息:" + body);
        String calrResult = null;
        try {
//			calrResult = Calculator.Instance.cal(body).toString();
            calrResult = "我们是好人";
        } catch (Exception e) {
            calrResult = "错误的表达式：" + e.getMessage();
        }
        ctx.write(Unpooled.copiedBuffer(calrResult.getBytes()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}