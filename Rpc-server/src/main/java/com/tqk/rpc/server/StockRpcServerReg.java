package com.tqk.rpc.server;


import com.tqk.rpc.server.rpc.RpcServerFrameReg;
import com.tqk.rpc.service.StockService;
import com.tqk.rpc.service.impl.StockServiceImpl;

/**
 *@author tianqikai
 *类说明：rpc的服务端，提库存供服务
 */
public class StockRpcServerReg {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RpcServerFrameReg serviceServer = new RpcServerFrameReg(9190);
                    //向注册中心注册服务
                    serviceServer.registerSerive(StockService.class, StockServiceImpl.class);
                    //启动rpc的服务端，提库存供服务
                    serviceServer.startService();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
