package com.tqk.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //加入一个netty 提供的httpServerCodec codec =>[coder - decoder]
        //HttpServerCodec 说明
        //1. HttpServerCodec 是netty 提供的处理http的 编-解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //2. 增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler", new HttpServerHandler());

        // 请求解码器
        socketChannel.pipeline().addLast("http-decoder", new HttpRequestDecoder());
        // 将HTTP消息的多个部分合成一条完整的HTTP消息
        socketChannel.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65535));
        // 响应转码器
        socketChannel.pipeline().addLast("http-encoder", new HttpResponseEncoder());
        // 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
        socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

        System.out.println("ok~~~~");

    }
}
