package com.tqk.netty.groupchat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 群聊服务端
 * @author tianqikai
 */
public class GroupChatServer {

    private int port; //监听端口


    public GroupChatServer(int port) {
        this.port = port;
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
                            //获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //向pipeline加入编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            //加入自己的业务处理handler
                            pipeline.addLast(new GroupChatServerHandler());

                        }
                    });
            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossNioEventLoopGroup.shutdownGracefully();
            workNioEventLoopGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(12345).run();
    }
}
