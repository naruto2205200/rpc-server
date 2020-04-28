package com.edu.vip;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangxiaofeng
 * @Describetion
 * @date 2020/4/2810:26
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Component
public @interface RpcService {
    Class<?> value(); //拿到服务的接口
    String version() default "";
}
