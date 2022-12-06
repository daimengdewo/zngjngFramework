package com.awen.energy.protocol.initializer;

import com.awen.energy.protocol.MessageCodec;
import com.awen.energy.protocol.message.SocketMsgHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private MessageCodec messageCodec;

    @Autowired
    private SocketMsgHandler socketMsgHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //添加对于读写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast(messageCodec);
        pipeline.addLast(new LengthFieldBasedFrameDecoder(64, 9, 1, 2, 0));
        pipeline.addLast(socketMsgHandler);
    }
}