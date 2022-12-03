package com.awen.energy.controller;

import com.awen.energy.handler.SocketMsgHandler;
import com.awen.feign.common.Code;
import com.awen.feign.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/energy")
public class TcpEnergyController {

    @GetMapping("/test")
    public Result queryById() {
        SocketMsgHandler.channelGroup.forEach(channel -> {
            System.out.println(channel.remoteAddress());
        });
        return new Result(true ? Code.GET_OK : Code.GET_ERR, null);
    }
}
