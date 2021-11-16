package com.tqk.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author tianqikai
 */
public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 9999;
        NettyServer echoServer = new NettyServer(port);
        System.out.println("服务器即将启动");
        echoServer.start();
        System.out.println("服务器关闭");
    }
    public void start() throws InterruptedException {
        NettyServerHandler nettyServerHandler = new NettyServerHandler();
        /*线程组*/
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            /*服务端启动必须*/
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(group).channel(NioServerSocketChannel.class)/*指定使用NIO进行网络传输*/
                    .localAddress(new InetSocketAddress(port))/*指定服务器监听端口*/
                    /*服务端每接收到一个连接请求，就会新启一个socket通信，也就是channel，
    所以下面这段代码的作用就是为这个子channel增加handle*/
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            /*添加到该子channel的pipeline的尾部*/
                            socketChannel.pipeline().addLast(nettyServerHandler);
                        }
                    });
            /*异步绑定到服务器，sync()会阻塞直到完成*/
            ChannelFuture f = serverBootstrap.bind().sync();
            /*阻塞直到服务器的channel关闭*/
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();/*优雅关闭线程组*/
        }
    }
}

