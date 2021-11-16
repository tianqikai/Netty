package com.tqk.nio.server;


/**
 * @author
 * 类说明：nio通信服务端
 */
public class NioServerWritable {

    public static int DEFAULT_PORT = 12345;
    public static String DEFAULT_SERVER_IP = "127.0.0.1";
    public static String response(String msg){
        return "Hello,"+msg+",Now is "+new java.util.Date(
                System.currentTimeMillis()).toString() ;
    }

    private static NioServerHandleWriteable nioServerHandle;

    public static void start(){
        if(nioServerHandle !=null) {
            nioServerHandle.stop();
        }
        nioServerHandle = new NioServerHandleWriteable(DEFAULT_PORT);
        new Thread(nioServerHandle,"Server").start();
    }
    public static void main(String[] args){
        start();
    }

}
