package com.tqk.dubbo.client;


import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.protocol.MessageProtocol;
import com.tqk.dubbo.server.util.JsonUtil;
import com.tqk.dubbo.service.StockService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MyZkClient {
    private static final String TYPE="1";
    /**
     * 注册服务
     * @throws InterruptedException
     * @return
     */
    public ChannelFuture initZkService( String servername,String ip,int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture=null;
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new MyZkInitializer()); //自定义一个初始化类
            channelFuture = bootstrap.connect("127.0.0.1", 8899).sync();

            putZkMsg(channelFuture, StockService.class.getName(),"127.0.0.1",9999);
            channelFuture.channel().closeFuture().sync();

        }finally {
            group.shutdownGracefully();

            return channelFuture;
        }
    }
    public void putZkMsg(ChannelFuture channelFuture, String servername,String ip,int port) throws InterruptedException {
        MessageObject messageObject = new MessageObject();
        messageObject.setServerName(servername);
        messageObject.setType(TYPE);
        messageObject.setIp(ip);
        messageObject.setPort(port);
        String mes= JsonUtil.toJSON(messageObject);
        System.out.println("发送消息："+mes);
        byte[] mesBytes = mes.getBytes(Charset.forName("utf-8"));
        int length = mes.getBytes(StandardCharsets.UTF_8).length;
        //创建协议包对象
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(mesBytes);
        messageProtocol.setLen(length);
        channelFuture.channel().writeAndFlush(messageProtocol);
    }

    public static void main(String[] args) throws InterruptedException {
        MyZkClient myZkClient = new MyZkClient();
        System.out.println("StockService:"+ StockService.class.getName());
        ChannelFuture channelFuture = myZkClient.initZkService(StockService.class.getName(),"127.0.0.1",9999);
    }
}
