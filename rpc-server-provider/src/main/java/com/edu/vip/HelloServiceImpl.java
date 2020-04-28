package com.edu.vip;

/**
 * @author naruto
 * @data 2020/4/27.
 */
@RpcService(value = IHelloService.class,version = "v1.0")
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("[v1.0]request in" +content);
        return "SUCCESS";
    }

    @Override
    public String saveUser(User user) {
        System.out.println("[v1.0]request in:"+user);
        return "SUCCESS";
    }
}
