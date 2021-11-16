package com.tqk.rpc.service;


import com.tqk.rpc.vo.UserInfo;

/**
 *
 *类说明：短信息发送接口
 */
public interface SendSms {

    boolean sendMail(UserInfo user);

}
