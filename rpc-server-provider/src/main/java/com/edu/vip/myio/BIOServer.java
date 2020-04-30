package com.edu.vip.myio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangxiaofeng
 * @Describetion
 * @date 2020/4/3016:56
 */
public class BIOServer {
    ServerSocket server;

    public BIOServer(int post) {
        try {
            server = new ServerSocket(post);
            System.out.println("Server 已启动，监听端口："+post);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while (true) {
            // 等待客户连接，接受信息  阻塞方法
            Socket client = server.accept();
            InputStream is = client.getInputStream();
            byte[] buff = new byte[1024];
            int len = is.read(buff);
            if (len > 0) {
                String msg = new String(buff, 0, len);
                System.out.println("收到："+msg);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new BIOServer(8081).listen();
    }
}
