package com.tqk.nettty.dubbo.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author tianqikai
 */
public class MyChannelInitializer extends ChannelInitializer {
    private  NettyRpcClientHandler client;

    public MyChannelInitializer(NettyRpcClientHandler client) {
        this.client=client;
    }
    public MyChannelInitializer() {

    }
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //编码解码
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new StringEncoder());
        pipeline.addLast(client);

    }
}
