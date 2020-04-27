package com.edu.vip;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        IHelloService helloService = new HelloServiceImpl();
        RpcProxyServer proxyServer = new RpcProxyServer();
        proxyServer.serverSocket(helloService,8080);

    }
}
