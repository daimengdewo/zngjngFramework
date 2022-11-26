package com.awen.user.controller;

import com.awen.user.common.Code;
import com.awen.user.common.Message;
import com.awen.user.common.Result;
import com.awen.user.config.PatternProperties;
import com.awen.user.entity.User;
import com.awen.user.service.UserService;
import com.awen.user.tool.ShiroCheck;
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

    @Autowired
    private PatternProperties properties;

    @GetMapping("test")
    public PatternProperties test() {
        return properties;
    }

    /**
     * 路径： /user/110
     *
     * @param id 用户id
     * @return 用户
     */
    @GetMapping("/{id}")
    @ShiroCheck(roles = "emp:add")
    public User queryById(@PathVariable("id") Long id) {
        return userService.queryById(id);
    }

    @GetMapping("/tokenError")
    public Result tokenError() {
        return new Result(Code.TOKEN_ERR, Message.TOKEN_ERR_MSG);
    }

    @GetMapping("/permission")
    public Result permissionError() {
        return new Result(Code.GET_AUTHORIZED_ERR, Message.AUTHORIZED_ERR_MSG);
    }
}
