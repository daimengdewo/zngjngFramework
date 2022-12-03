package com.awen.energy.protocol;

import cn.hutool.core.text.StrSplitter;
import com.awen.energy.protocol.message.DeviceMessage;
import com.awen.energy.tool.NumTools;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, DeviceMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, DeviceMessage message, List<Object> list) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeByte(0x68);
        byteBuf.writeBytes(message.getDeviceid());
        byteBuf.writeByte(0x68);
        byteBuf.writeBytes(new byte[]{0x11, 0x04, 0x33, 0x34, 0x35, 0x35, 0x1C, 0x16});
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        //设备报文
        String msg = ByteBufUtil.hexDump(byteBuf);
        //分割处理
        String[] msgList = StrSplitter.splitByLength(msg, 2);
        //报文数据提取
        NumTools.reverse(msgList);
        
        byteBuf.skipBytes(byteBuf.readableBytes());
        byteBuf.retain();
    }
}
