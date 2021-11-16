package com.tqk.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 使用 FileChannel(通道) 和 方法  transferFrom ，完成文件的拷贝
 * @author tianqikai
 */
public class NioFileChannel04 {
    public static void main(String[] args) throws IOException {
        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("bio02.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("bio03.jpg");

        //获取各个流对应的filechannel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        destCh.transferFrom(sourceCh,0,sourceCh.size());
        //关闭相关通道和流
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
