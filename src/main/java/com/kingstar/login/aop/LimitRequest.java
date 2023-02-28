package com.kingstar.login.aop;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRequest {
    // 限制时间 单位：分钟，这里默认1分钟
    String time() default "1";
    // 允许请求的次数
    String count() default "10";
    // 是否走配置文件，默认不走配置文件
    boolean isFlag() default false;
}
