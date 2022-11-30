package com.awen.feign.tool;

import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/global")
public class GlobalResult {


    @GetMapping("/tokenError")
    public Result tokenError() {
        return new Result(Code.TOKEN_ERR, Message.TOKEN_ERR_MSG);
    }

    @GetMapping("/rolesError")
    public Result rolesError() {
        return new Result(Code.GET_AUTHORIZED_ERR, Message.AUTHORIZED_ERR_MSG);
    }
}
