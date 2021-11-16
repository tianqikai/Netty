package com.tqk.netty.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author tianqikai
 */
public class NettyClient {
    private final int port;
    private final String host;

    public NettyClient(int port, String host) {
        this.port = port;
        this.host = host;
    }
    public void start() throws InterruptedException {
        //线程组
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //客户端启动必备
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)/*把线程组传入*/
                    /*指定使用NIO进行网络传输*/
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new NettyClientHandle());
            /*连接到远程节点，阻塞直到连接完成*/
            ChannelFuture future = bootstrap.connect().sync();
            /*阻塞程序，直到Channel发生了关闭*/
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully().sync();
        }

    }
    public static void main(String[] args) throws InterruptedException {
        new NettyClient(9999,"127.0.0.1").start();
//        Channel
    }
}
