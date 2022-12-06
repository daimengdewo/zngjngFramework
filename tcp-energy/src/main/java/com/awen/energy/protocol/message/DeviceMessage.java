package com.awen.energy.protocol.message;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class DeviceMessage implements Serializable {
    private byte[] deviceId;
    private byte[] deviceData;
    private byte[] last;

    /**
     * 定义全局单利channel组 管理所有channel
     */
    private static volatile ChannelGroup channelGroup = null;

    /**
     * 存放请求ID与channel的对应关系
     */
    private static volatile ConcurrentHashMap<String, Channel> channelMap = null;

    /**
     * 定义两把锁
     */
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();


    public static ChannelGroup getChannelGroup() {
        if (null == channelGroup) {
            synchronized (lock1) {
                if (null == channelGroup) {
                    channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                }
            }
        }
        return channelGroup;
    }

    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        if (null == channelMap) {
            synchronized (lock2) {
                if (null == channelMap) {
                    channelMap = new ConcurrentHashMap<>();
                }
            }
        }
        return channelMap;
    }

    public static Channel getChannel(String deviceid) {
        if (null == channelMap) {
            return getChannelMap().get(deviceid);
        }
        return channelMap.get(deviceid);
    }
}