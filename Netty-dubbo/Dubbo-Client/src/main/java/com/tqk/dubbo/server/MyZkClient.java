package com.tqk.dubbo.server;


import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.protocol.MessageProtocol;
import com.tqk.dubbo.server.protocol.MyMessageDecoder;
import com.tqk.dubbo.server.protocol.MyMessageEncoder;
import com.tqk.dubbo.server.util.JsonUtil;
import com.tqk.dubbo.service.StockService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class MyZkClient {
    private static MyZkHandler zkclient;
    private static ChannelFuture zkchannelFuture = null;
    private int count = 0;
    private int zkport = 8899;
    /**
     * 初始化消费者
     */
    public void initClient(int port,MyZkHandler client,String providerName){
        ChannelFuture channelFuture = null;

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new MyMessageEncoder()); //加入编码器
                        pipeline.addLast(new MyMessageDecoder()); //加入解码器
                        pipeline.addLast(client);
                    }
                }); //自定义一个初始化类

        try {
            channelFuture = bootstrap.connect("127.0.0.1", port).sync();

            getAddress(channelFuture,providerName);
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void getAddress(ChannelFuture zkchannelFuture, String providerName){
        //得到channel
        Channel channel = zkchannelFuture.channel();
        MessageObject messageObject = new MessageObject();
        messageObject.setServerName(providerName);
        messageObject.setType("2"); //2-获取服务列表
        String mes= JsonUtil.toJSON(messageObject);
        System.out.println("向注册中心发送消息："+mes);
        byte[] mesBytes = mes.getBytes(Charset.forName("utf-8"));
        int length = mes.getBytes(StandardCharsets.UTF_8).length;
        MessageProtocol messageProtocol = new MessageProtocol();

        messageProtocol.setContent(mesBytes);
        messageProtocol.setLen(length);
        channel.writeAndFlush(messageProtocol);
    }

    public static void main(String[] args) {
       new MyZkClient().initClient(8899,new MyZkHandler(), StockService.class.getName());
       System.out.println( "返回值 map:"+MyDubboHandler.getServiceHolder().toString());
       System.out.println( "返回值 map:"+MyZkHandler.getServiceHolder().toString());

    }
}
