package com.example.io.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author daizhichao
 * @date 2020/5/29
 */
public class Test {
    //    public static void main(String[] args) {
//        try {
//            inputNIOChannel();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void inputNIOChannel() throws IOException {
        //创建一个File实例
        File file = new File("C:/var/test.txt");
        //FileInputStream为文件输入流
        FileInputStream in = new FileInputStream(file);
        //缓冲器向通道输入数据
        FileChannel fileChannel = in.getChannel();
        //创建一个容量为1024字节的ByteBuffer
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //写入数据到Buffer
        int bytesRead = fileChannel.read(buf);
        while (bytesRead != -1) {
            //回绕缓冲区（输出通道会从数据的开头而不是末尾开始）
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            /**
             * 压缩此缓冲区，compact方法会执行两个动作
             * 1.清除之前写好的字符
             * 2.通过标记位置为0
             * 这就为什么要结合filp()使用
             */
            buf.compact();
            //写入数据到Buffer
            bytesRead = fileChannel.read(buf);
        }
    }

    public static void main(String[] args) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(new File("C:/var/test.txt"), "rw");
            ByteBuffer buf = ByteBuffer.allocate(400);  //设置缓冲区
            FileChannel inChannel = raf.getChannel();   //从流中获取通道
            int bytesRead = 0;
            do {
                if (bytesRead != -1) {        //第一次为0 必读，到尾部后不再读取
                    bytesRead = inChannel.read(buf);
                }
                readLine(buf, "UTF-8", 100);
                //确保缓冲区内的内容被读取完毕
            } while (buf.position() != 0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readLine(ByteBuffer buf, String encode, int size) throws Exception {
        byte[] b = new byte[size];  //设置储存一行内容的数组
        buf.flip();                //将读取标识position致0开始读取
        int i = 0;
        while (buf.hasRemaining()) {  //判断 position < limit
            byte tempbyte = buf.get();
            b[i] = tempbyte;
            i++;
            if (tempbyte == 10) {    //如果该字节是10 （换行）跳出循环
                break;
            }
            if (i == b.length) {
                b = Arrays.copyOf(b, b.length * 2); //如果临时数组不够大，扩容2倍
            }
        }
        b = Arrays.copyOf(b, i);         //临时数组未被填充的位置默认为0，我们要排除这些数据。
        System.out.print(new String(b, "UTF-8"));
        //将未读取内容放到缓冲区头部，并将position设置为尾部（及下次读取从上次尾部开始拼接缓冲区）
        buf.compact();
        return new String(b, encode);
    }
}
