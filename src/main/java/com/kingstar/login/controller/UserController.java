package com.kingstar.login.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingstar.login.aop.LimitRequest;
import com.kingstar.login.bean.User;
import com.kingstar.login.service.UserService;
import com.kingstar.login.utils.TokenUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private HttpSession session;

    //其中count指的是规定时间内的访问次数，表示该接口在我们设置的默认时间内只能访问3次，默认时间在自定义注解里面设置的
    @LimitRequest(isFlag = true)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getCode())) {
            map.put("msg", "手机号或验证码为空！");
            return map;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone());
//        User userDb = userService.getOne(queryWrapper);
        //----------
        User userDb = new User();
        userDb.setPhone(user.getPhone());
        userDb.setCode(user.getCode());
        //----------

        if (userDb != null) {
            String token = TokenUtil.generateToken(userDb);
            map.put("code", 1);
            map.put("data", userDb);
            map.put("token", token);
            session.setAttribute("phone", userDb.getPhone());
        } else {
            map.put("msg", "用户名或密码错误！");
        }
        return map;
    }

    @RequestMapping(value = "/getById", method = {RequestMethod.POST, RequestMethod.GET})
    public User getById(Long id, String name) {
        System.out.println(name);
        return userService.getById(id);
    }

    @RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
    public String test(@RequestParam("name") String name) {
        System.out.println(name);
        return name;
    }

    @RequestMapping(value = "/getMap", method = {RequestMethod.POST, RequestMethod.GET})
    public Map<String, Object> getMap() {

        return TokenUtil.tokenMap();
    }

}
