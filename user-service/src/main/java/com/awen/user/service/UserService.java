package com.awen.user.service;

import com.awen.user.entity.User;

public interface UserService {
    User queryById(Long id);
}