package com.tqk.dubbo.server;

import com.tqk.dubbo.client.MyZkClient;
import com.tqk.dubbo.service.StockService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author tianqikai
 */
public class MyDubboServer {
    public static void main(String[] args) throws InterruptedException {
        runserver();
    }

    private static void runserver() throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new MyDubboServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
            System.out.println("服务端启动成功！！！！");
            //注册中心注册服务
            MyZkClient myZkClient = new MyZkClient();
            System.out.println("StockService:"+StockService.class.getName());
            ChannelFuture channelFuture1 = myZkClient.initZkService(StockService.class.getName(),"127.0.0.1",9999);

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
