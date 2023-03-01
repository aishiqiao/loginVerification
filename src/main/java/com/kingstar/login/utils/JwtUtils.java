package com.kingstar.login.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;


public class JwtUtils {

    private static final String SECRET_STR = "unicenter-file";

    public static String createToken(String applicationName, String[] acceptanceFormList) {
        Date expiresDate = Date.from(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());

        return JWT.create().withAudience(applicationName)
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate)
                .withArrayClaim("acceptanceFormList", acceptanceFormList)
                .sign(Algorithm.HMAC256(SECRET_STR));
    }

    /**
     * 校验token是否过期
     *
     * @param token
     * @return
     */
    public static boolean verifyTokenIssuedAt(String token) {
        try {
            return JWT.decode(token).getExpiresAt().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验token
     *
     * @param token
     * @return
     */
    public static boolean verifyToken(String token) {
        try {
            // 通过密钥进行解密
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_STR)).build();
            /**
             * 通过 verifier.verify() 方法检验 token，如果token不符合则抛出异常
             */
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String[] getAcceptanceFormList(String token) {
        try {
            return JWT.decode(token).getClaim("acceptanceFormList").asArray(String.class);
        } catch (Exception e) {
            throw new JWTDecodeException("token解析失败");
        }
    }

    public static String getAudience(String token) {
        try {
            return JWT.decode(token).getAudience().get(0);
        } catch (Exception e) {
            throw new JWTDecodeException("token解析失败");
        }
    }
}
