package com.tqk.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用 FileChannel(通道) 和 方法  read , write，完成文件的拷贝
 * @author tianqikai
 */
public class NioFileChannel03 {
    public static void main(String[] args) throws Exception {
        File file=new File("FileChannel01.txt");
        //创建文件的输入流
        FileInputStream fileInputStream = new FileInputStream("FileChannel01.txt");
        //通过fileInputStream 获取对应的FileChannel -> 实际类型  FileChannelImpl
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        //将 通道的数据读入到Buffer   read == -1//表示读完
        int read = fileInputStreamChannel.read(byteBuffer);

        //创建文件的输出流
        FileOutputStream fileOutputStream = new FileOutputStream("FileChannel02.txt");
        //通过fileOutputStream 获取对应的FileChannel -> 实际类型  FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        byteBuffer.flip();
        fileChannel.write(byteBuffer);

        //关闭相关的流
        fileInputStreamChannel.close();
        fileChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }
}
