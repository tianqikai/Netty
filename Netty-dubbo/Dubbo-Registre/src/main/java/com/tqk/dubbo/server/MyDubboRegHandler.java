package com.tqk.dubbo.server;

import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.protocol.MessageProtocol;
import com.tqk.dubbo.server.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @author tianqikai
 */
public class MyDubboRegHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    //key表示服务名，value代表服务提供者地址的集合
    private static final Map<String,List<RegisterServiceVo>> serviceHolder = new HashMap<>();

    private  int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据，并处理
        int len = msg.getLen();
        byte[] content = msg.getContent();
        String message = new String(content, Charset.forName("utf-8"));
        System.out.println();
        System.out.println("服务器接收到信息如下");
        System.out.println("长度=" + len);
        System.out.println("内容=" + message);

        MessageObject messageObject= (MessageObject)JsonUtil.toBean(message, MessageObject.class);
        if(messageObject.getType().equals("1")){
            //1-标识注册服务
            registerSerive(messageObject.getServerName(),messageObject.getIp(),messageObject.getPort());

            String str="服务已注册["+messageObject.getServerName()+"]，" + "地址["+messageObject.getIp()+"]，端口["+messageObject.getPort()+"]";
            MessageObject msgObject=new MessageObject();
            msgObject.setType("1");
            msgObject.setContext(str);
            String response=JsonUtil.toJSON(msgObject);

            int responseLen = response.getBytes("utf-8").length;
            byte[]  responseContent = response.getBytes("utf-8");
            //构建一个协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(responseLen);
            messageProtocol.setContent(responseContent);
            ctx.writeAndFlush(messageProtocol);
        }else{
            //2-表示获取服务
            List<RegisterServiceVo> list=getService(messageObject.getServerName());

            String str=JsonUtil.toJSON(list);

            MessageObject msgObject=new MessageObject();
            msgObject.setType("2");
            msgObject.setContext(str);
            msgObject.setServerName(messageObject.getServerName());
            String response=JsonUtil.toJSON(msgObject);
            System.out.println("向消费方返回信息: " + response);

            int responseLen = response.getBytes("utf-8").length;
            byte[]  responseContent2 = response.getBytes("utf-8");
            //构建一个协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(responseLen);
            messageProtocol.setContent(responseContent2);
            ctx.writeAndFlush(messageProtocol);
            System.out.println("向消费方返回信息: " + messageProtocol.toString());
            ctx.close();
        }
        System.out.println("服务器接收到消息: " + messageObject.toString());
        System.out.println("服务器接收到消息包数量=" + (++this.count));

    }
    /**
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {

            //将  evt 向下转型 IdleStateEvent
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";

                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";

                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--超时时间--" + eventType);
            System.out.println("服务器做相应处理..");

            //如果发生空闲，我们关闭通道
            // ctx.channel().close();
        }
    }

    /**
     * 取出服务提供者
     * @param serviceName
     * @return
     */
    private static List<RegisterServiceVo> getService(String serviceName){
        return serviceHolder.get(serviceName);
    }
    /**
     * 服务注册，考虑到可能有多个提供者同时注册，进行加锁
     * @param serviceName
     * @param host
     * @param port
     */
    private static synchronized void registerSerive(String serviceName, String host,int port){
        //获得当前服务的已有地址集合
        List<RegisterServiceVo> serviceVoSet = serviceHolder.get(serviceName);
        if(serviceVoSet==null){
            //已有地址集合为空，新增集合
            serviceVoSet = new ArrayList<>();
            serviceHolder.put(serviceName,serviceVoSet);
        }
        //将新的服务提供者加入集合
        serviceVoSet.add(new RegisterServiceVo(host,port));
        System.out.println("服务已注册["+serviceName+"]，" + "地址["+host+"]，端口["+port+"]");
    }
}
