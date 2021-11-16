package com.tqk.netty.tcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * @author tianqikai
 */
public class MyTcpClientHandler1 extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("服务器返回信息："+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //使用客户端发送10条数据 hello,server 编号
        for(int i= 0; i< 10; ++i) {
            System.out.println("循环次数："+i);
            ByteBuf buffer = Unpooled.copiedBuffer("hello,server " + i, Charset.forName("utf-8"));
//            ctx.writeAndFlush(buffer);
            ctx.writeAndFlush("小明哈哈");
        }
    }
}
