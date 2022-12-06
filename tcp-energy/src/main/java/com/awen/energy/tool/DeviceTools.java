package com.awen.energy.tool;

import cn.hutool.core.text.StrSplitter;
import com.awen.energy.entity.ChannelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

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
        return String.join("", deviceId);
    }

    public byte[] reverseDeviceId(String id) {
        //分割处理
        String[] msgList = StrSplitter.splitByLength(id, 2);
        //取出表号
        ArrayList<String> deviceId = new ArrayList<>();
        byte[] bytes = Arrays.toString(deviceId.toArray()).getBytes();
        //翻转
        for (int i = 0; i < msgList.length; i++) {
            deviceId.add(0, msgList[i]);
        }
        System.out.println("表号：" + deviceId);
        System.out.println("byte表号：" + Arrays.toString(bytes));
        return bytes;
    }

    //报文标识
    public String reverseDeviceDataName(ArrayList<String> msgList) {
        //取出数据
        ArrayList<String> deviceDataName = new ArrayList<>();
        //翻转
        for (int i = 0; i < 4; i++) {
            deviceDataName.add(String.format("%x", Long.parseLong(msgList.get(10 + i), 16)));
        }
        return String.join("", deviceDataName);
    }

    public byte[] reverseDeviceDataName(String data) {
        //分割处理
        String[] msgList = StrSplitter.splitByLength(data, 2);
        ArrayList<String> deviceDataName = new ArrayList<>(Arrays.asList(msgList));
        byte[] bytes = Arrays.toString(deviceDataName.toArray()).getBytes();
        System.out.println("数据标识：" + deviceDataName);
        System.out.println("bytes数据：" + Arrays.toString(bytes));
        return bytes;
    }

    //报文正文
    public Double reverseDeviceData(ArrayList<String> msgList) {
        //取出数据
        ArrayList<String> deviceData = new ArrayList<>();
        int data_length = Integer.parseInt(msgList.get(9)) - 4;
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

    public byte[] check(String deviceId, String deviceData) {
        String[] msgList = StrSplitter.splitByLength("68" + deviceId + "68" + deviceData, 2);
        long num = 0L;
        for (int i = 0; i < msgList.length - 2; i++) {
            num = num + Long.parseLong(msgList[i], 16);
        }
        String tag = Long.toHexString(num);
        return new byte[]{Byte.parseByte(tag.substring(tag.length() - 2)), 0x16};
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
