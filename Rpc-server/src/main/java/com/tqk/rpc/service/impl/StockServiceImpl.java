package com.tqk.rpc.service.impl;

import com.tqk.dubbo.server.util.JsonUtil;
import com.tqk.rpc.service.StockService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 *@author tianqikai
 *
 *类说明：库存数量变动服务的实现
 */
public class StockServiceImpl implements StockService {

	//存放库存数据
    private static ConcurrentHashMap<String,Integer> goodsData =
            new ConcurrentHashMap<String, Integer>();

    static {
        goodsData.put("A001",1000);
        goodsData.put("B002",2000);
        goodsData.put("C003",3000);
        goodsData.put("D004",4000);
    }

    @Override
    public synchronized void addStock(String goodsId, int addAmout) {
        System.out.println("+++++++++++++++++增加商品："+goodsId+"的库存,数量为："+addAmout);
        int amount = goodsData.get(goodsId)+addAmout;
        goodsData.put(goodsId,amount);
        System.out.println("+++++++++++++++++商品："+goodsId+"的库存,数量变为："+amount);
    }

    @Override
    public synchronized void deduceStock(String goodsId, int deduceAmout) {
        System.out.println("-------------------减少商品："+goodsId+"的库存,数量为："+ deduceAmout);
        int amount = goodsData.get(goodsId)- deduceAmout;
        goodsData.put(goodsId,amount);
        System.out.println("-------------------商品："+goodsId+"的库存,数量变为："+amount);
    }
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String json="{\"args\":[\"A001\",1000],\"context\":\"\",\"ip\":\"\",\"methodName\":\"addStock\",\"parmTypes\":[\"java.lang.String\",\"int\"],\"port\":0,\"serverName\":\"com.tqk.rpc.service.StockService\",\"type\":\"3\"}";
        MessageObject messageObject = JsonUtil.toBean(json, MessageObject.class);
        System.out.println(messageObject.getServerName());
        System.out.println(messageObject.getIp());
        System.out.println(messageObject.getArgs());
        System.out.println(messageObject.getMethodName());
        Class stockService = Class.forName(messageObject.getServerName());
        Method method = stockService.getMethod(messageObject.getMethodName(), messageObject.getParmTypes());
        method.invoke(stockService.newInstance(),messageObject.getArgs());

    }
}
class MessageObject {
    private String type;
    private String serverName;
    private String ip;
    private int port;
    private String methodName;
    private String context;
    //方法的入参类型
    private Class<?>[] parmTypes;
    //方法入参的值
    private Object[] args ;

    @Override
    public String toString() {
        return "MessageObject{" +
                "type='" + type + '\'' +
                ", serverName='" + serverName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", methodName='" + methodName + '\'' +
                ", context='" + context + '\'' +
                ", parmTypes=" + Arrays.toString(parmTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    public MessageObject() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Class<?>[] getParmTypes() {
        return parmTypes;
    }

    public void setParmTypes(Class<?>[] parmTypes) {
        this.parmTypes = parmTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
