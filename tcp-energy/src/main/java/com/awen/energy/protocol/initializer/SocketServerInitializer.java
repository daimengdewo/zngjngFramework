package com.awen.energy.protocol.initializer;

import com.awen.energy.handler.SocketMsgHandler;
import com.awen.energy.protocol.MessageCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    private SocketMsgHandler socketMsgHandler;
    @Autowired
    private MessageCodec messageCodec;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //添加对于读写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合
        pipeline.addLast(new HttpObjectAggregator(1024 * 64));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(64, 9, 1, 2, 0));
        pipeline.addLast(messageCodec);
        pipeline.addLast(new LoggingHandler());
    }
}