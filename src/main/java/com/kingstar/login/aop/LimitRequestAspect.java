package com.kingstar.login.aop;
import com.alibaba.fastjson.JSON;
import com.kingstar.login.bean.User;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LimitRequestAspect {

    private static final Logger logger = LoggerFactory.getLogger(LimitRequestAspect.class);
    private static ConcurrentHashMap<String, ExpiringMap<String, Integer>> book = new ConcurrentHashMap<>();


    private final BeanExpressionContext exprContext;
    private final BeanExpressionResolver exprResolver;

    //=============通过配置的路径获取数据=========================开始
    public LimitRequestAspect(ConfigurableBeanFactory beanFactory) {
        this.exprContext = new BeanExpressionContext(beanFactory, (Scope)null);
        this.exprResolver = beanFactory.getBeanExpressionResolver();
    }
    public String resolveStringValue(String strVal) {
        String value = this.exprContext.getBeanFactory().resolveEmbeddedValue(strVal);
        if (this.exprResolver != null && value != null) {
            Object evaluated = this.exprResolver.evaluate(value, this.exprContext);
            value = evaluated != null ? evaluated.toString() : null;
        }
        return value;
    }
    //=============通过配置的路径获取数据=========================结束

    // 让所有有@LimitRequest注解的方法都执行切面方法
    @Pointcut("@annotation(limitRequest)")
    public void excudeService(LimitRequest limitRequest) {
    }

    @Around("excudeService(limitRequest)")
    public Object doAround(ProceedingJoinPoint pjp, LimitRequest limitRequest) throws Throwable {

        // 获得request对象
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        // 获取用户ip
        String ipStr = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
            ipStr = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
            ipStr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
            ipStr = request.getRemoteAddr();
        }
        // 获取用户id
        String ip = null;
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            if (arg instanceof User) {
                User user = (User) arg;
                ip = user.getIp();
            }
        }
        if (StringUtils.isBlank(ip)) {
            ip = request.getParameter("userId");
        }
        logger.info("===========切面获取的用户ip："+ip);

        // 不同接口的唯一标识key
        String key = StringUtils.join(request.getRequestURI(),"-",ipStr,"-",ip);
        logger.info("===========切面获取的唯一key："+key);

        // 获取Map对象， 如果没有则返回默认值
        // 第一个参数是key， 第二个参数是默认值
        ExpiringMap<String, Integer> map = book.getOrDefault(request.getRequestURI(), ExpiringMap.builder().variableExpiration().build());
        Integer uCount = map.getOrDefault(request.getRemoteAddr(), 0);

        String count = limitRequest.count();
        if (uCount >= Integer.valueOf( limitRequest.count())) { // 超过次数，不执行目标方法
            //这里的返回对象类型根据controller方法的返回方式一致
            HashMap<Integer, String> integerStringHashMap = new HashMap<>();
            return integerStringHashMap.put(500,"接口访问次数超过限制,请稍后重试");
        } else if (uCount == 0){ // 第一次请求时，设置开始有效时间
            map.put(request.getRemoteAddr(), uCount + 1, ExpirationPolicy.CREATED, Integer.valueOf(limitRequest.time())*60*1000L, TimeUnit.MILLISECONDS);
        } else { // 未超过次数， 记录数据加一
            map.put(request.getRemoteAddr(), uCount + 1);
        }
        book.put(request.getRequestURI(), map);

        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();

        return result;
    }

}
