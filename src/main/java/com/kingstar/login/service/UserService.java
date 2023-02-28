package com.kingstar.login.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kingstar.login.bean.User;

public interface UserService {

    User getOne(QueryWrapper<User> queryWrapper);

    User getById(Long id);
}
