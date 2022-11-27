package com.awen.user.service.impl;

import com.awen.user.entity.User;
import com.awen.user.mapper.UserMapper;
import com.awen.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryById(Long id) {
        return userMapper.findById(id);
    }
}
