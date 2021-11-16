package com.tqk.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 类说明：自己的业务处理
 * @author tianqikai
 */
@ChannelHandler.Sharable

/**
 * 不加这个注解那么在增加到childHandler时就必须new出来
 *添加一个StatusHandler，目的为了记录同时在线的设备数量
 *private StatusHandler statusHandler = new StatusHandler();
**/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 服务端收到数据以后，就会执行
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        System.out.println("Server accept"+in.toString(CharsetUtil.UTF_8));
        ctx.write(in);

    }

    /**
     * 服务端读取完成网络数据后的处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 发生异常后的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
