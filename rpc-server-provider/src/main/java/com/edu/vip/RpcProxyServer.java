package com.edu.vip;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author naruto
 * @data 2020/4/27.
 */
public class RpcProxyServer {
    ExecutorService executorService = Executors.newCachedThreadPool();
    public void serverSocket (Object service,int port){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new ProcessHandler(service,socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
