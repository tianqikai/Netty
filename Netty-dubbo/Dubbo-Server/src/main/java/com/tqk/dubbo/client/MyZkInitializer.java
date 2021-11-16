package com.tqk.dubbo.client;

import com.tqk.dubbo.server.protocol.MyMessageDecoder;
import com.tqk.dubbo.server.protocol.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * @author tianqikai
 */
public class MyZkInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageEncoder()); //加入编码器
        pipeline.addLast(new MyMessageDecoder()); //加入解码器
        pipeline.addLast(new MyZkHandler());
    }
}
