package com.edu.vip;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangxiaofeng
 * @Describetion
 * @date 2020/4/2810:36
 */
@ComponentScan(basePackages ="com.edu.vip" )
@Configuration
public class SpringConfig {
    @Bean(name = "gpRpcServer")
    public GpRpcServer gpRpcServer(){
        return new GpRpcServer(8080);
    }
}
