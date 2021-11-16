package com.tqk.dubbo.server.protocol;

import java.util.Arrays;

/**
 * @author tianqikai
 */
public class MessageObject {
    private String type;
    private String serverName;
    private String ip;
    private int port;
    private String methodName;
    private String context;
    //方法的入参类型
    private Class<?>[] parmTypes;
    //方法入参的值
    private Object[] args ;

    @Override
    public String toString() {
        return "MessageObject{" +
                "type='" + type + '\'' +
                ", serverName='" + serverName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", methodName='" + methodName + '\'' +
                ", context='" + context + '\'' +
                ", parmTypes=" + Arrays.toString(parmTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    public MessageObject() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Class<?>[] getParmTypes() {
        return parmTypes;
    }

    public void setParmTypes(Class<?>[] parmTypes) {
        this.parmTypes = parmTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
