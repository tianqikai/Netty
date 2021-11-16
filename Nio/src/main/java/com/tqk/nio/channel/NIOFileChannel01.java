package com.tqk.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 使用channels将buffer-》file
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str="hello，world";
        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("FileChannel01.txt");
        //通过 fileOutputStream 获取 对应的 FileChannel
        //这个 fileChannel 真实 类型是  FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个缓冲区 ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //将 str 放入 byteBuffer
        buffer.put(str.getBytes(StandardCharsets.UTF_8));
        //对byteBuffer 进行flip
        buffer.flip();
        //将byteBuffer 数据写入到 fileChannel
        channel.write(buffer);

        channel.close();
        fileOutputStream.close();
    }
}
