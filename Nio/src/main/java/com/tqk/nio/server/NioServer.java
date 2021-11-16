package com.tqk.nio.server;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 类说明：nio通信服务端
 */
public class NioServer {
    public static int DEFAULT_PORT = 12345;
    public static String DEFAULT_SERVER_IP = "127.0.0.1";
    public static String response(String msg){
        return "Hello,"+msg+",Now is "+new java.util.Date(
                System.currentTimeMillis()).toString() ;
    }
    private static NioServerHandle nioServerHandle;
    public static void start(){
        if(nioServerHandle !=null) {
            nioServerHandle.stop();
        }
        nioServerHandle = new NioServerHandle(DEFAULT_PORT);
        new Thread(nioServerHandle,"Server").start();
    }
    public static void main(String[] args){
        start();
    }

}
