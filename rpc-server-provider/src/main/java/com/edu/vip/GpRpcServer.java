package com.edu.vip;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangxiaofeng
 * @Describetion
 * @date 2020/4/2810:35
 */
@Component
public class GpRpcServer implements ApplicationContextAware, InitializingBean {

    ExecutorService executorService = Executors.newCachedThreadPool();
    private Map<String, Object> handler = new HashMap<>();
    private int port;

    public GpRpcServer(int port) {
        this.port = port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new ProcessHandler(handler,socket));
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!beansWithAnnotation.isEmpty()) {
            for (Object serviceBean : beansWithAnnotation.values()) {
                // 拿到注解
                RpcService annotation = serviceBean.getClass().getAnnotation((RpcService.class));
                String name = annotation.value().getName(); // 接口类
                String version = annotation.version(); //版本号
                if(!StringUtils.isEmpty(version)){
                    name +="-"+version;
                }
                handler.put(name,serviceBean);
            }
        }
    }
}
