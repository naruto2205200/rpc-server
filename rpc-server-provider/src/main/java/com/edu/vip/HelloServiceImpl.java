package com.edu.vip;

/**
 * @author naruto
 * @data 2020/4/27.
 */
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("request in" +content);
        return "SUCCESS";
    }

    @Override
    public String saveUser(User user) {
        System.out.println("request in:"+user);
        return "SUCCESS";
    }
}
