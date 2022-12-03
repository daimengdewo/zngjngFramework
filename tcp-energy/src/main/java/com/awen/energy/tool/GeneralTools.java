package com.awen.energy.tool;

import com.awen.energy.entity.ChannelData;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 常用代码抽象
 */
@Component
public class GeneralTools {
    @Autowired
    MapperMenu mapperMenu = new MapperMenu();

    public Integer duplicateDeviceid(String deviceid) {
        LambdaQueryWrapper<ChannelData> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChannelData::getDeviceid, deviceid);
        return mapperMenu.getChannelGroupMapper().selectCount(wrapper);
    }
}
