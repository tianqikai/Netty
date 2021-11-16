package com.tqk.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 本地文件读数据
 * @author tianqikai
 */
public class NioFileChannel02 {
    public static void main(String[] args) throws IOException {

        File file=new File("FileChannel01.txt");
        //创建文件的输入流
        FileInputStream fileInputStream = new FileInputStream("FileChannel01.txt");
        //通过fileInputStream 获取对应的FileChannel -> 实际类型  FileChannelImpl
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //将 通道的数据读入到Buffer
        fileInputStreamChannel.read(byteBuffer);
        String s = new String(byteBuffer.array());
        //将byteBuffer 的 字节数据 转成String
        System.out.println(s);

        fileInputStreamChannel.close();
        fileInputStream.close();
    }
}
