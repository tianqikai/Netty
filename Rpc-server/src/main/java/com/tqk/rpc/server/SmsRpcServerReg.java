package com.tqk.rpc.server;

import com.tqk.rpc.server.rpc.RpcServerFrameReg;
import com.tqk.rpc.service.SendSms;
import com.tqk.rpc.service.impl.SendSmsImpl;
/**
 *@author tianqikai
 *
 *类说明：rpc的服务端，提供短信服务
 */
public class SmsRpcServerReg {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RpcServerFrameReg serviceServer = new RpcServerFrameReg(9189);
                    //向注册中心注册服务
                    serviceServer.registerSerive(SendSms.class, SendSmsImpl.class);
                    //启动rpc的服务端，短信服务
                    serviceServer.startService();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

