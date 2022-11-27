package com.awen.user.controller;

import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.common.Result;
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

    @GetMapping("/tokenError")
    public Result tokenError() {
        return new Result(Code.TOKEN_ERR, Message.TOKEN_ERR_MSG);
    }

    @GetMapping("/rolesError")
    public Result permissionError() {
        return new Result(Code.GET_AUTHORIZED_ERR, Message.AUTHORIZED_ERR_MSG);
    }
}
