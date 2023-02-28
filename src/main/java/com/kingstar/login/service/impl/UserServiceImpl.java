package com.kingstar.login.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingstar.login.bean.User;
import com.kingstar.login.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getOne(QueryWrapper<User> queryWrapper) {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }
}
