package com.awen.energy.controller;

import com.awen.energy.entity.ChannelData;
import com.awen.energy.protocol.message.DeviceMessage;
import com.awen.feign.common.Code;
import com.awen.feign.common.Result;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/energy")
public class TcpEnergyController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/test")
    public Result test(@RequestBody ChannelData channelData) {
        String address = redisTemplate.opsForValue().get(channelData.getDeviceid());
        Channel channel = DeviceMessage.getChannel(channelData.getDeviceid());
        if (Objects.isNull(channel)) {
            throw new RuntimeException("未连接socket服务器");
        }
        channel.writeAndFlush(new byte[]{0x68, 0x72, 0x67, 0x08, 0x01, 0x07, 0x31, 0x68, 0x11, 0x04, 0x33, 0x34, 0x34, 0x35, (byte) 0xCF, 0x16});
        return new Result(Code.GET_OK, null);
    }
}
