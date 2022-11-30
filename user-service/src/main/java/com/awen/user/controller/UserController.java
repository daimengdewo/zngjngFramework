package com.awen.user.controller;

import com.awen.feign.tool.ShiroCheck;
import com.awen.user.entity.User;
import com.awen.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @ShiroCheck(roles = "test")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }
}
