package com.kingstar.login.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 目标方法执行前被调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 获取访问的 URI
        String uri = request.getRequestURI();
        log.info("preHandle拦截到的请求的URI={}", uri);

        //2. 进行登录校验
        String loginAdmin = request.getHeader("loginAdmin");

        if (null != loginAdmin && TokenUtil.checkCacheName(loginAdmin)) {//说明该用户已经成功登录过了
            return true;
        }
        try {
            //如果验证token失败，并且方法注明了Authorization，返回401错误
                //设置response状态
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");

                //返回的数据
                JSONObject res = new JSONObject();
                res.put("status","-1");
                res.put("msg","need login");
                PrintWriter out = null ;
                out = response.getWriter();
                out.write(res.toString());
                out.flush();
                out.close();
                return false;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle() 执行了...");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion() 执行了...");
    }
}

