package com.kingstar.login.controller;

import com.kingstar.login.bean.User;
import com.kingstar.login.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

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

}
