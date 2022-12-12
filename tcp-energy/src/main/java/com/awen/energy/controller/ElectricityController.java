package com.awen.energy.controller;

import com.awen.energy.entity.ChannelData;
import com.awen.energy.protocol.SendType;
import com.awen.energy.service.ChannelGroupService;
import com.awen.feign.common.Code;
import com.awen.feign.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/electricity")
public class ElectricityController {

    @Autowired
    private ChannelGroupService channelGroupService;

    //开闸
    @PostMapping("/valveUp")
    public Result valveUp(@RequestBody ChannelData channelData) {
        channelGroupService.channelGroupUtil(channelData.getDeviceid(), SendType.VALVE_UP);
        return new Result(Code.COMMON_OK, null);
    }

    //合闸
    @PostMapping("/valveDown")
    public Result valveDown(@RequestBody ChannelData channelData) {
        channelGroupService.channelGroupUtil(channelData.getDeviceid(), SendType.VALVE_DOWN);
        return new Result(Code.COMMON_OK, null);
    }

    //单相电流
    @PostMapping("/one_current")
    public Result one_current(@RequestBody ChannelData channelData) {
        channelGroupService.channelGroupUtil(channelData.getDeviceid(), SendType.ONE_PHASE_CURRENT);
        return new Result(Code.COMMON_OK, null);
    }

    //test
    @PostMapping("/test")
    public Result test(@RequestBody ChannelData channelData) {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyMMdd");
        System.out.println(LocalDate.now().format(date));

        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(LocalTime.now().format(time));
        return new Result(Code.COMMON_OK, null);
    }
}
