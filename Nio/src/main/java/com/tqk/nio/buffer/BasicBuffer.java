package com.tqk.nio.buffer;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        //创建一个Buffer，大小为5，既可以存放5个int
        IntBuffer allocate = IntBuffer.allocate(5);

        // 向Buffer存放数据
        for (int i=0;i< allocate.capacity();i++){
            allocate.put(i*3);
        }
//        for (int i=0;i< allocate.capacity();i++){
//            System.out.println(allocate.get(i));
//        }
        System.out.println("-------------------");
        /**
         *     从buffer读取数据。需要使用flip进行读写切换
         *     public final Buffer flip() {
         *         limit = position;读取数据不能超过五个
         *         position = 0;
         *         mark = -1;
         *         return this;
         *     }
         */
        allocate.flip();
        allocate.position(1);
        allocate.limit(4);
        while(allocate.hasRemaining()){
            System.out.println(allocate.get());
        }
    }
}
