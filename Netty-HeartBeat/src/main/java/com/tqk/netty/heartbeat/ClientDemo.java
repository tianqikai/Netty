package com.tqk.netty.heartbeat;

import com.tqk.netty.groupchat.client.GroupChatClient;

public class ClientDemo {
    public static void main(String[] args) throws Exception {
        new GroupChatClient("127.0.0.1", 7000).run();
    }
}
