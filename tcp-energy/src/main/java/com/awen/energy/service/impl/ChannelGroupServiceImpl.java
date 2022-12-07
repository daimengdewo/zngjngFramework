package com.awen.energy.service.impl;

import com.awen.energy.entity.ChannelData;
import com.awen.energy.mapper.ChannelGroupMapper;
import com.awen.energy.protocol.SendType;
import com.awen.energy.protocol.message.DeviceMessage;
import com.awen.energy.service.ChannelGroupService;
import com.awen.energy.tool.DeviceTools;
import com.awen.energy.tool.MapperMenu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class ChannelGroupServiceImpl extends ServiceImpl<ChannelGroupMapper, ChannelData> implements ChannelGroupService {

    @Autowired
    private MapperMenu mapperMenu;

    @Autowired
    private DeviceTools deviceTools;

    @Override
    public CompletableFuture<Boolean> channelGroupUtil(String deviceid, SendType sendType) {
        //获取通道对象
        Channel channel = DeviceMessage.getChannel(deviceid);
        //判断是否连接
        if (Objects.isNull(channel)) {
            throw new RuntimeException("未连接socket服务器");
        }
        //生成指令
        byte[] bytes = deviceTools.encodeCommand(deviceid, sendType.getType());
        //发送
        channel.writeAndFlush(bytes);
        return CompletableFuture.completedFuture(true);
    }


}
