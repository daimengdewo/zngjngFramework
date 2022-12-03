package com.awen.energy.service;

import com.awen.energy.entity.ChannelData;
import com.awen.feign.tool.FunctionMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.CompletableFuture;

public interface ChannelGroupService extends IService<ChannelData> {
    CompletableFuture<Boolean> channelGroupUtil(ChannelData channelData, FunctionMenu menu);
}
