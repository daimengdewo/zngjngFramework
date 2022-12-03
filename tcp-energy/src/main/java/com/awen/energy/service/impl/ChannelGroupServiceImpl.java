package com.awen.energy.service.impl;

import com.awen.energy.entity.ChannelData;
import com.awen.energy.mapper.ChannelGroupMapper;
import com.awen.energy.service.ChannelGroupService;
import com.awen.energy.tool.MapperMenu;
import com.awen.feign.tool.FunctionMenu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ChannelGroupServiceImpl extends ServiceImpl<ChannelGroupMapper, ChannelData> implements ChannelGroupService {

    @Autowired
    private MapperMenu mapperMenu;

    @Override
    public CompletableFuture<Boolean> channelGroupUtil(ChannelData channelData, FunctionMenu menu) {
        switch (menu) {
            case ADD:
                mapperMenu.getChannelGroupMapper().insert(channelData);
                break;
            case UPDATE:
                break;
        }
        return null;
    }


}
