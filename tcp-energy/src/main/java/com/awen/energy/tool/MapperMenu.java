package com.awen.energy.tool;

import com.awen.energy.mapper.ChannelGroupMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class MapperMenu {
    @Autowired
    private ChannelGroupMapper channelGroupMapper;
}
