package com.tqk.dubbo.service;

import com.tqk.dubbo.server.protocol.MessageObject;
import com.tqk.dubbo.server.util.JsonUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        String json="{\"args\":[\"A001\",1000],\"context\":\"\",\"ip\":\"\",\"methodName\":\"addStock\",\"parmTypes\":[\"java.lang.String\",\"int\"],\"port\":0,\"serverName\":\"com.tqk.dubbo.service.StockService\",\"type\":\"3\"}";
        MessageObject messageObject = JsonUtil.toBean(json, MessageObject.class);
        System.out.println(messageObject.getServerName());
        System.out.println(messageObject.getIp());
        System.out.println(messageObject.getArgs());
        System.out.println(messageObject.getMethodName());
        Class<?> stockService = Class.forName(messageObject.getServerName()+"Impl");
        Method method = stockService.getMethod(messageObject.getMethodName(), messageObject.getParmTypes());
        method.invoke(stockService.newInstance(),messageObject.getArgs());

    }
}
