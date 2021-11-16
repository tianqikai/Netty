package com.tqk.dubbo.server;

import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.protocol.MessageProtocol;
import com.tqk.dubbo.server.util.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author tianqikai
 */
public class MyDubboServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
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

        MessageObject messageObject= (MessageObject) JsonUtil.toBean(message, MessageObject.class);
        String serverName = messageObject.getServerName();
        String methodName = messageObject.getMethodName();
        Class<?>[] parmTypes = messageObject.getParmTypes();
        Object[] args = messageObject.getArgs();
        System.out.println("--------------------调用服务------------------");
        Class zClass = Class.forName(serverName+"Impl");
        if (zClass == null){
            throw new ClassNotFoundException(zClass+" Not Found");
        }
        Method method = zClass.getMethod(methodName,parmTypes);

        method.invoke(zClass.newInstance(),args);

        System.out.println("服务器接收到消息包数量=" + (++this.count));
        Object result = method.invoke(zClass.newInstance(),args);
        //        回复消息
        MessageObject msgObject=new MessageObject();
        msgObject.setType("1");
        msgObject.setContext("服务处理成功！");
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

        ctx.writeAndFlush(messageProtocol);
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
}
