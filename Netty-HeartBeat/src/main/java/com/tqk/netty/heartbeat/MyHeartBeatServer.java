package com.tqk.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


public class MyHeartBeatServer {
    private int part;

    public MyHeartBeatServer(int part) {
        this.part = part;
    }

    public MyHeartBeatServer() {
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup bossNioEventLoopGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workNioEventLoopGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossNioEventLoopGroup,workNioEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty 提供 IdleStateHandler
                            /*
                            说明
                            1. IdleStateHandler 是netty 提供的处理空闲状态的处理器
                            2. long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接
                            3. long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
                            4. long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接

                            5. 文档说明
                            triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                            * read, write, or both operation for a while.
                            6. 当 IdleStateEvent 触发后 , 就会传递给管道 的下一个handler去处理
                            通过调用(触发)下一个handler 的 userEventTiggered , 在该方法中去处理 IdleStateEvent(读空闲，写空闲，读写空闲)
                             */
                            pipeline.addLast(new IdleStateHandler(3,3,7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new MyHeartBeatServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossNioEventLoopGroup.shutdownGracefully();
            workNioEventLoopGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new MyHeartBeatServer().run();
    }
}
