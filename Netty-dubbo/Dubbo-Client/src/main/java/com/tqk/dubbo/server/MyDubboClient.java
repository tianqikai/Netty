package com.tqk.dubbo.server;



import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.protocol.MyMessageDecoder;
import com.tqk.dubbo.server.protocol.MyMessageEncoder;
import com.tqk.dubbo.service.StockService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;



import java.lang.reflect.Proxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyDubboClient {
    //创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static MyDubboHandler client;
    private static MyZkClient myZkClient;
    private int count = 0;
    private int port = 0000;
    //编写方法使用代理模式，获取一个代理对象

    public Object getBean(final Class<?> serivceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serivceClass}, (proxy, method, args) -> {

                    System.out.println("(proxy, method, args) 进入...." + (++count) + " 次");
                    //连接注册中心，获取服务端信息
//                    if(myZkClient==null){
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                myZkClient=new MyZkClient();
//                                myZkClient.initClient(8899,new MyZkHandler(), StockService.class.getName());
//
//                            }
//                        }).start();
//                    }
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//                    port=MyZkHandler.getServiceHolder().get(providerName).get(0).getPort();
//                    System.out.println("PORT:"+port);
                    //连接服务端
                    if (client == null) {
                        initClient(9999);
                    }

                    //设置要发给服务器端的信息
                    //providerName 协议头 args[0] 就是客户端调用api hello(???), 参数
                    MessageObject messageObject = new MessageObject();
                    messageObject.setType("3");
                    messageObject.setServerName(providerName);
                    messageObject.setMethodName(method.getName());
                    messageObject.setArgs(args);
                    messageObject.setParmTypes(method.getParameterTypes());
                    client.setMessageObject(messageObject);

                    //使用ExecutorService执行Callable类型的任务，并返回结果
                    return executor.submit(client).get();

                });
    }

    /**
     * 初始化客户端
     * @param port
     */
    private static void initClient(int port) {
        client = new MyDubboHandler();
        //创建EventLoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast(new MyMessageDecoder());
                                pipeline.addLast(new MyMessageEncoder());
                                pipeline.addLast(client);
                            }
                        }
                );
        try {
            bootstrap.connect("127.0.0.1", port).sync();
            System.out.println("连接服务器成功");
        } catch (Exception e) {
            System.out.println("连接服务器失败！");
            e.printStackTrace();
        }
    }
}
