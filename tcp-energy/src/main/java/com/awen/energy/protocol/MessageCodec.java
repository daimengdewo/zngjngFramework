package com.awen.energy.protocol;

import cn.hutool.core.text.StrSplitter;
import com.awen.energy.protocol.message.DeviceMessage;
import com.awen.energy.tool.DeviceTools;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, DeviceMessage> {

    @Autowired
    private DeviceTools deviceTools;

    @Override
    protected void encode(ChannelHandlerContext ctx, DeviceMessage message, List<Object> list) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        System.out.println(byteBuf);
        byteBuf.writeByte(0x68);
        byteBuf.writeBytes(message.getDeviceid());
        byteBuf.writeByte(0x68);
        System.out.println(byteBuf);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        //设备报文
        String msg = ByteBufUtil.hexDump(byteBuf);
        //分割处理
        String[] msgList = StrSplitter.splitByLength(msg, 2);
//        if (deviceTools.check(msgList)) {
//            //获取ip
//            SocketAddress address = ctx.channel().remoteAddress();
//            //报文设备id提取
//            String deviceId = deviceTools.reverseDeviceId(msgList);
//            //ChannelData对象
//            ChannelData channelData = new ChannelData();
//            channelData.setAddress(address.toString());
//            channelData.setDeviceid(deviceId);
//            //缓存
//            deviceTools.saveChannelData(channelData);
//
//            //报文标识提取
//            String deviceDataName = deviceTools.reverseDeviceDataName(msgList);
//            //报文数据提取
//            String deviceData = deviceTools.reverseDeviceData(msgList);
//
//            System.out.println(Arrays.toString(msgList));
//            System.out.println(deviceId);
//            System.out.println(deviceDataName);
//            System.out.println(deviceData);
//
//
//            DeviceMessage.getChannelMap().put(deviceId, ctx.channel());
//            // 将deviceId作为自定义属性加入到channel中，方便随时channel中获取
//            AttributeKey<String> key = AttributeKey.valueOf("deviceId");
//            ctx.channel().attr(key).setIfAbsent(deviceId);
//        }
        list.add(msgList);
    }
}
