package com.tqk.netty.tcp.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author tianqikai
 */
public class MyTcpServerHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private  int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("Server accept: "+msg.toString(CharsetUtil.UTF_8));
    }
}
