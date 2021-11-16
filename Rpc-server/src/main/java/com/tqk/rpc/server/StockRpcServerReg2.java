package com.tqk.rpc.server;


import com.tqk.rpc.server.rpc.RpcServerFrameReg;
import com.tqk.rpc.service.StockService;
import com.tqk.rpc.service.impl.StockServiceImpl;

/**
 *@author Mark老师   享学课堂 https://enjoy.ke.qq.com 
 *
 *更多课程咨询 依娜老师  QQ：2470523467  VIP课程咨询 依娜老师  QQ：2470523467
 *
 *类说明：rpc的服务端，提库存供服务
 */
public class StockRpcServerReg2 {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RpcServerFrameReg serviceServer = new RpcServerFrameReg(9192);
                    serviceServer.registerSerive(StockService.class, StockServiceImpl.class);
                    serviceServer.startService();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
