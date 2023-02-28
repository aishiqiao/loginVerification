package com.kingstar.login;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 类描述-
 * @Author junqi
 * @Date 2021/3/12
 **/
@Slf4j
public class test {

    private static final ConcurrentHashMap<String, String> POOLS = new ConcurrentHashMap<>();

    /*
     *加载外部配置文件
     */

    @Test
    public void test(){


    }

    @Test
    public void Form_Load() {

    }

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};



    @Test
    public void ConcurrentHashMap() {

    }
}
