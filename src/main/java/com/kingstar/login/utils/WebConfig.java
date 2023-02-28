package com.kingstar.login.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //注册自定义拦截器LoginInterceptor
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") //这里拦截所有的请求
//                .excludePathPatterns("/","/login","/images/**"); //指定要放行的，根据业务需要来添加放行的请求路径
                .excludePathPatterns("/","/user/login"); //指定要放行的，根据业务需要来添加放行的请求路径

    }
}


