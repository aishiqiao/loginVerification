package com.kingstar.login.utils;


import com.kingstar.login.bean.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenUtil {


    private static final Map<String, Object> tokenMap = new HashMap<>();
    /**
     * 每个缓存生效时间2小时 2 * 60 * 60 * 1000L
     */
    public static final long CACHE_HOLD_TIME_2H = 30 * 60 * 1000L;

    public static String generateToken(User user) {
        //生成唯一不重复的字符串
        String token = UUID.randomUUID().toString();
        put(token, user, CACHE_HOLD_TIME_2H);
        return token;
    }

    /**
     * 验证token是否合法
     */
    public static boolean verify(String token) {
        return tokenMap.containsKey(token);
    }

    public static Object gentUser(String token) {
        return tokenMap.get(token);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(UUID.randomUUID().toString());
        }
    }

    /**
     * 存放一个缓存对象，保存时间为holdTime
     */
    public static void put(String cacheName, Object obj, long holdTime) {
        if (checkCacheName(cacheName)) {
            return;
        }
        tokenMap.put(cacheName, obj);
        //缓存失效时间
        tokenMap.put(cacheName + "_HoldTime", System.currentTimeMillis() + holdTime);
    }

    /**
     * 删除所有缓存
     */
    public static void removeAll() {
        tokenMap.clear();
    }

    /**
     * 删除某个缓存
     */
    public static void remove(String cacheName) {
        tokenMap.remove(cacheName);
        tokenMap.remove(cacheName + "_HoldTime");
    }

    /**
     * 检查缓存对象是否存在，
     * 若不存在，则返回false
     * 若存在，检查其是否已过有效期，如果已经过了则删除该缓存并返回false
     */
    public static boolean checkCacheName(String cacheName) {
        Long cacheHoldTime = (Long) tokenMap.get(cacheName + "_HoldTime");
        if (cacheHoldTime == null || cacheHoldTime == 0L) {
            return false;
        }
        if (cacheHoldTime < System.currentTimeMillis()) {
            remove(cacheName);
            return false;
        }
        return tokenMap.containsKey(cacheName);
    }

    public static Map<String, Object> tokenMap() {
        return tokenMap;
    }

    // 添加定时任务
    @Scheduled(cron = "0 */5 * * * ?")
    public void doTask() {
        if (!tokenMap.isEmpty()) {
            tokenMap.forEach((key, value) -> {
                if (!key.contains("HoldTime")) {
                    checkCacheName(key);
                }
            });
        }
    }
}