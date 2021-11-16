package com.tqk.rpc.client;

import com.tqk.rpc.client.rpc.RpcClientFrameReg;
import com.tqk.rpc.service.SendSms;
import com.tqk.rpc.service.StockService;
import com.tqk.rpc.vo.UserInfo;

/**
 *
 * @author tianqikai
 */
public class RpcClientReg {
    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo("tianqikai","18562328330");
        //发送短信接口调用
        SendSms sendSms = RpcClientFrameReg.getRemoteProxyObj(SendSms.class);
        System.out.println("Send mail: "+ sendSms.sendMail(userInfo));
        //变动库存服务接口调用
        StockService stockService = RpcClientFrameReg.getRemoteProxyObj(StockService.class);
        //增加库存
        stockService.addStock("A001",1000);
        //减少库存
        stockService.deduceStock("B002",50);
    }
}
