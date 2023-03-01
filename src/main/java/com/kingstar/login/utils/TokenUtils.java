package com.kingstar.login.utils;

import com.kingstar.login.bean.UserEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
public class TokenUtils {

    public final static Map<String, UserEntity> TOKEN_CACHE = new HashMap<>();

    synchronized public String addToken(String phone,String code,String ip){
        UserEntity userEntity = new UserEntity();
        userEntity.setPhone(phone);
        userEntity.setCode(code);
        userEntity.setIp(ip);
        String token = UUID.randomUUID().toString();
            for (String s : TOKEN_CACHE.keySet()) {
                if (TOKEN_CACHE.get(s).getPhone().equals(phone)) {
                    TOKEN_CACHE.remove(s);
                    break;
                }
            }

        TOKEN_CACHE.put(token,userEntity);
        return token;
    }

}
