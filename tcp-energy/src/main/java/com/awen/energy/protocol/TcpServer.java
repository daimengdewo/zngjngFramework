package com.awen.energy.protocol;

import com.awen.energy.protocol.message.DeviceMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

public class TcpServer {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(64, 9, 1, 2, 0),
                new LoggingHandler(),
                new MessageCodec());

        DeviceMessage message = new DeviceMessage();
        message.setDeviceId(new byte[]{0x31, 0x07, 0x01, 0x08, 0x67, 0x72});

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();

        channel.writeInbound(buffer);
    }
}
