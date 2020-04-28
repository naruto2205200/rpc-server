package com.edu.vip;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author naruto
 * @data 2020/4/27.
 */
public class ProcessHandler implements Runnable{

    private Socket socket;
//    private Object service;
    private Map<String, Object> handler = new HashMap<>();
//    public ProcessHandler(Object service,Socket socket) {
//        this.service = service;
//        this.socket = socket;
//    }
    public ProcessHandler(Map<String, Object> handler,Socket socket) {
        this.handler = handler;
        this.socket = socket;
    }
    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 反射调用本地服务
            Object result = invoke(rpcRequest);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RpcRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceName = request.getClassName();
        String version  = request.getVersion();
        if(!StringUtils.isEmpty(version)){
            serviceName +="-"+version;
        }
        Object service = handler.get(serviceName);
        if (service == null) {
            throw new RuntimeException("Service not found:" + serviceName);
        }
        // 拿到客户端请求的参数
        Object[] args = request.getParameters();
        // 根据请求的类进行加载
        Class clazz = Class.forName(request.getClassName());
        Object result = null;
        if(args!=null){
            // 获得每个参数的类型
            Class<?>[] types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
            // 找到请求的类中的方法
            Method method = clazz.getMethod(request.getMethodName(), types);
            // 进行反射调用
            result = method.invoke(service, args);
        }else{
            // 找到请求的类中的方法
            Method method = clazz.getMethod(request.getMethodName());
            // 进行反射调用
            result = method.invoke(service, args);
        }
        return result;
    }
}
