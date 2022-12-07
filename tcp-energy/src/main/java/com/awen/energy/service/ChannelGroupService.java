package com.awen.energy.service;

import com.awen.energy.entity.ChannelData;
import com.awen.energy.protocol.SendType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.CompletableFuture;

public interface ChannelGroupService extends IService<ChannelData> {
    CompletableFuture<Boolean> channelGroupUtil(String deviceid, SendType sendType);
}
