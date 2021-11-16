package com.tqk.nio.channel;

import java.nio.ByteBuffer;

/**
 * 可以将一个普通Buffer 转成只读Buffer
 * @author tianqikai
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {

        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(64);

        for(int i = 0; i < 10; i++) {
            buffer.put((byte)i);
        }
        //读取
        buffer.flip();
        //得到一个只读的Buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        //读取
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte)100); //报错 ReadOnlyBufferException
    }
}