package com.tqk.nettty.dubbo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyRpcClient {
    //创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyRpcClientHandler client;
    private int count = 0;

    public Object getBean(final Class<?> serviceClass, final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("(proxy, method, args) 进入...." + (++count) + " 次");
                //{}  部分的代码，客户端每调用一次 hello, 就会进入到该代码
                if (client == null) {
                    initClient();
                }

                //设置要发给服务器端的信息
                //providerName 协议头 args[0] 就是客户端调用api hello(???), 参数
                client.setPara(providerName + args[0]);

                //使用ExecutorService执行Callable类型的任务，并返回结果
                return executor.submit(client).get();
            }
        });
    }
    /**
     * 初始化netty客户端
     */
    public static void initClient(){
        client=new NettyRpcClientHandler();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new MyChannelInitializer(client));
        try {
            bootstrap.connect("127.0.0.1",7000).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    }
}
