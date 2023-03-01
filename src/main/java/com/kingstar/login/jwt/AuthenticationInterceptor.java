package com.kingstar.login.jwt;

import com.alibaba.fastjson.JSON;
import com.kingstar.login.bean.CommonResult;
import com.kingstar.login.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("请求进入");
        String accessToken = request.getHeader("x-unicenter-ks-token");
        if (StringUtils.isBlank(accessToken)){
            return false;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler  ;
        Method method = handlerMethod.getMethod();
        // 服务令牌校验
        if (method.isAnnotationPresent(AccessToken.class)) {
            // 校验令牌是否存在
            if (accessToken == null || StringUtils.isEmpty(accessToken)) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(CommonResult.blank(205,"无令牌",null)));
                return false;
            }
            // 校验令牌是否被篡改
            if (!JwtUtils.verifyToken(accessToken)) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(CommonResult.blank(205,"令牌异常",null)));
                return false;
            }
            // 校验令牌是否过时
            if (!JwtUtils.verifyTokenIssuedAt(accessToken)) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(CommonResult.blank(205,"令牌过时",null)));
                return false;
            }
        }
        return true;
    }
}
