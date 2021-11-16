package com.tqk.dubbo.server;

import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.protocol.MessageProtocol;
import com.tqk.dubbo.server.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author tianqikai
 */
public class MyZkHandler extends SimpleChannelInboundHandler<MessageProtocol>  {
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
    private String para;

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

        //注册服务返回信息
        String str = messageObject.getContext();
        List<RegisterServiceVo> registerServiceVos = JsonUtil.toList(str, RegisterServiceVo.class);
        serviceHolder.put(messageObject.getServerName(), registerServiceVos);
        System.out.println("服务查询成功=" + str);
        System.out.println( "返回值 map:"+MyZkHandler.getServiceHolder().toString());
        for (String key: MyZkHandler.getServiceHolder().keySet()) {
            List<RegisterServiceVo> value = serviceHolder.get(key);
            System.out.println("key:"+key+" vlaue:"+value.get(0).getPort());
        }
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

