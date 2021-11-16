package com.tqk.dubbo.server;

import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.protocol.MessageProtocol;
import com.tqk.dubbo.server.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author tianqikai
 */
public class MyDubboHandler extends SimpleChannelInboundHandler<MessageProtocol> implements Callable {
    //key表示服务名，value代表服务提供者地址的集合
    private static final Map<String, List<RegisterServiceVo>> serviceHolder = new HashMap<>();

    private int count;

    public static Map<String, List<RegisterServiceVo>> getServiceHolder() {
        return serviceHolder;
    }

    //上下文
    private ChannelHandlerContext context;
    //返回的结果
    private String result;
    //客户端调用方法时，传入的参数
    private MessageObject messageObject;

    /**
     * 与服务器的连接创建后，就会被调用, 这个方法是第一个被调用(1)
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(" channelActive 被调用  ");
        //因为我们在其它方法会使用到 ctx
        context = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    /**
     * 被代理对象调用, 发送数据给服务器，-> wait -> 等待被唤醒(channelRead) -> 返回结果 (3)-》5
     *
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        System.out.println(" call1 被调用  ");
        String mes= JsonUtil.toJSON(messageObject);
        System.out.println("向服务提供中心发送消息："+mes);
        byte[] mesBytes = mes.getBytes(Charset.forName("utf-8"));
        int length = mes.getBytes(StandardCharsets.UTF_8).length;
        MessageProtocol messageProtocol = new MessageProtocol(length,mesBytes);
        context.writeAndFlush(messageProtocol);
        //进行wait
        wait(); //等待channelRead 方法获取到服务器的结果后，唤醒
        System.out.println(" call2 被调用  ");
        //服务方返回的结果
        return result;

    }

    /**
     * (2)
     *
     * @param messageObject
     */
    void setMessageObject(MessageObject messageObject) {
        System.out.println(" messageObject : " + messageObject.toString());
        this.messageObject = messageObject;
    }

    /**
     * 收到服务器的数据后，调用方法 (4)
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println(" channelRead 被调用  ");
        result = msg.toString();
        System.out.println(" channelRead 被调用2  ");
        int len = msg.getLen();
        byte[] content = msg.getContent();
        String jsonStr = new String(content, Charset.forName("utf-8"));
        System.out.println("内容=" + jsonStr);
        MessageObject messageObject = JsonUtil.toBean(jsonStr, MessageObject.class);
        System.out.println("服务端返回信息：" + messageObject.getContext());
        if (messageObject.getType().equals("1")) {
            //服务提供者返回信息
            System.out.println("服务端返回信息：" + messageObject.getContext());
        } else {
            //注册服务返回信息
            String str = messageObject.getContext();
            List<RegisterServiceVo> registerServiceVos = JsonUtil.toList(str, RegisterServiceVo.class);
            serviceHolder.put(messageObject.getServerName(), registerServiceVos);
            System.out.println("服务查询成功=" + str);
        }
        notify(); //唤醒等待的线程
        System.out.println("客户端接收消息数量=" + (++this.count));

    }

    /**
     * 获取注册服务
     *
     * @param serviceName
     * @param registerServiceVos
     */
    private static synchronized void registerSerive(String serviceName, List<RegisterServiceVo> registerServiceVos) {
        //获得当前服务的已有地址集合
        List<RegisterServiceVo> serviceVoSet = serviceHolder.get(serviceName);

        if (serviceVoSet == null) {
            //已有地址集合为空，新增集合
            serviceHolder.put(serviceName, registerServiceVos);
            System.out.println("获取注册服务1:" + serviceHolder.get(serviceName).toString());
        } else {
            serviceHolder.remove(serviceName);
            serviceHolder.put(serviceName, registerServiceVos);
            System.out.println("获取注册服务2:" + serviceHolder.get(serviceName).toString());
        }
    }
}

