package com.tqk.dubbo.server;

import com.tqk.dubbo.server.protocol.MyMessageDecoder;
import com.tqk.dubbo.server.protocol.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

/**
 * @author tianqikai
 */
public class MyDubboServerInitializer extends ChannelInitializer<SocketChannel> {
    //创建业务线程池
    //这里我们就创建2个子线程
    static final EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(2);

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(300,300,900, TimeUnit.SECONDS));
        pipeline.addLast(new MyMessageDecoder());//解码器
        pipeline.addLast(new MyMessageEncoder());//编码器
        pipeline.addLast(executorGroup,new MyDubboServerHandler());
    }
}
