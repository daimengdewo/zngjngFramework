package com.awen.energy.tool;

import cn.hutool.core.text.StrSplitter;
import com.awen.energy.entity.ChannelData;
import com.google.common.primitives.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DeviceTools {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //报文表号
    public String reverseDeviceId(ArrayList<String> msgList) {
        //取出表号
        ArrayList<String> deviceId = new ArrayList<>();
        //翻转
        for (int i = 1; i < 7; i++) {
            deviceId.add(0, msgList.get(i));
        }
        //转字符串
        return String.join("", deviceId);
    }

    public byte[] encodeCommand(String id, String data) {
        //分割处理
        String[] deviceId = StrSplitter.splitByLength("68" + id + "68", 2);
        String[] device_data = StrSplitter.splitByLength(data, 2);
        String[] device = StrSplitter.splitByLength("68" + id + "68" + data, 2);
        //byte数组
        ArrayList<Byte> bytes = new ArrayList<>();
        //遍历
        for (String s : deviceId) {
            bytes.add(0, (byte) Long.parseLong(s, 16));
        }
        for (String s : device_data) {
            bytes.add((byte) Long.parseLong(s, 16));
        }
        long num = 0L;
        for (String s : device) {
            num = num + Long.parseLong(s, 16);
        }
        String tag = Long.toHexString(num);
        //校验位
        bytes.add((byte) Long.parseLong(tag.substring(tag.length() - 2), 16));
        //固定16结束
        bytes.add((byte) Long.parseLong("16", 16));
        return Bytes.toArray(bytes);
    }

    //报文标识
    public String reverseDeviceDataName(ArrayList<String> msgList) {
        //取出数据
        ArrayList<String> deviceDataName = new ArrayList<>();
        //读取长度
        long data_length = Long.parseLong(msgList.get(9), 16);
        //判断类型
        if (data_length < 4) {
            deviceDataName.add(String.format("%x", Long.parseLong(msgList.get(10), 16)));
        } else {
            //翻转
            for (int i = 0; i < 4; i++) {
                deviceDataName.add(String.format("%x", Long.parseLong(msgList.get(10 + i), 16)));
            }
        }
        //转字符串
        return String.join("", deviceDataName);
    }

    //报文正文
    public Double reverseDeviceData(ArrayList<String> msgList) {
        //取出数据
        ArrayList<String> deviceData = new ArrayList<>();
        long data_length = Long.parseLong(msgList.get(9), 16) - 4;
        //翻转
        for (int i = 0; i < data_length; i++) {
            deviceData.add(0, String.format("%x", Long.parseLong(msgList.get(14 + i)) - 33));
        }
        String data = String.join("", deviceData);
        //判空
        if (!data.equals("")) {
            return Double.valueOf(data);
        } else {
            return 0.0;
        }
    }

    //检查数据完整性
    public boolean check(ArrayList<String> msgList) {
        long num = 0L;
        for (int i = 0; i < msgList.size() - 2; i++) {
            num = num + Long.parseLong(msgList.get(i), 16);
        }
        long num2 = Long.parseLong(msgList.get(msgList.size() - 2), 16);
        String tag1 = Long.toHexString(num);
        String tag2 = Long.toHexString(num2);
        return tag1.substring(tag1.length() - 2).equals(tag2);
    }

    //缓存到redis
    public void saveChannelData(ChannelData channelData) {
        //判断该表号是否存在于缓存中
        if (Boolean.TRUE.equals(redisTemplate.hasKey(channelData.getDeviceid()))) {
            //有则更新
            redisTemplate.delete(channelData.getDeviceid());
            redisTemplate.opsForValue().set(channelData.getDeviceid(), channelData.getAddress());
        } else {
            //无则新增
            redisTemplate.opsForValue().set(channelData.getDeviceid(), channelData.getAddress());
        }
    }

    //保存到mysql
    public void saveDeviceData(ChannelData channelData) {
        //判断该表号是否存在于缓存中
        if (Boolean.TRUE.equals(redisTemplate.hasKey(channelData.getDeviceid()))) {
            //有则更新
            redisTemplate.delete(channelData.getDeviceid());
            redisTemplate.opsForValue().set(channelData.getDeviceid(), channelData.getAddress());
        } else {
            //无则新增
            redisTemplate.opsForValue().set(channelData.getDeviceid(), channelData.getAddress());
        }
    }
}
