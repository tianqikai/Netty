package com.tqk.dubbo.client;


import com.tqk.dubbo.server.MyDubboClient;
import com.tqk.dubbo.service.StockService;

/**
 *
 * @author tianqikai
 */
public class ClientBootstrap {
    public static void main(String[] args) {


        MyDubboClient myDubboClient = new MyDubboClient();
        //变动库存服务接口调用
        StockService stockService = (StockService) myDubboClient.getBean(StockService.class,StockService.class.getName());
        //增加库存
        stockService.addStock("A001",1000);
        //减少库存
//        stockService.deduceStock("B002",50);
    }
}
