package com.tqk.netty.protocoltcp.server;

import com.tqk.netty.protocoltcp.client.MyMessageDecoder;
import com.tqk.netty.protocoltcp.client.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author tianqikai
 */
public class MyProtocolTcpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageDecoder());//解码器
        pipeline.addLast(new MyMessageEncoder());//编码器
        pipeline.addLast(new MyProtocolTcpServerHandler());
    }
}
