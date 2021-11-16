package com.tqk.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        String reqStr = "我是客户端请求tianqikai";
        myNettyClient(reqStr);
    }

    private static void myNettyClient(String reqStr) throws InterruptedException {
        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            //注意客户端使用的不是 ServerBootstrap 而是 Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类(反射)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // marshalling 序列化对象的解码
//                  socketChannel.pipeline().addLast(MarshallingCodefactory.buildDecoder());
                            // marshalling 序列化对象的编码
//                  socketChannel.pipeline().addLast(MarshallingCodefactory.buildEncoder());
                            // 处理来自服务端的响应信息
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            System.out.println("客户端 ok..");

            //启动客户端去连接服务器端
            //关于 ChannelFuture 要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();

            // 发送客户端的请求
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(reqStr.getBytes(CharsetUtil.UTF_8)));

            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {

            group.shutdownGracefully();

        }
    }
}
