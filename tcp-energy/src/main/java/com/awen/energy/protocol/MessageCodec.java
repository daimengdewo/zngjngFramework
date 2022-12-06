package com.awen.energy.protocol;

import cn.hutool.core.text.StrSplitter;
import com.awen.energy.protocol.message.DeviceMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, DeviceMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, DeviceMessage message, List<Object> list) throws Exception {
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        //设备报文
        String msg = ByteBufUtil.hexDump(byteBuf);
        //分割处理
        String[] msgList = StrSplitter.splitByLength(msg, 2);
        //去除FE
        ArrayList<String> newMsgList = new ArrayList<>();
        for (String s : msgList) {
            if (!Objects.equals(s, "fe")) {
                newMsgList.add(s);
            }
        }
        //传递给下一个处理器
        list.add(newMsgList);
    }
}
