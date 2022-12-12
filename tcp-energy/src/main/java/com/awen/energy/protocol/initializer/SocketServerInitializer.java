package com.awen.energy.protocol.initializer;

import com.awen.energy.protocol.MessageCodec;
import com.awen.energy.protocol.message.SocketMsgHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
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
        //byte
        pipeline.addLast(new ByteArrayEncoder());
        //String
        pipeline.addLast(new StringEncoder());
        //消息解码
        pipeline.addLast(messageCodec);
//        //心跳检测
//        pipeline.addLast(new IdleStateHandler(5, 0, 0));
//        //心跳事件处理
//        pipeline.addLast(new ChannelDuplexHandler() {
//            @Override
//            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//                IdleStateEvent event = (IdleStateEvent) evt;
//                if (event.state() == IdleState.READER_IDLE) {
//                    System.out.println("心跳超时！");
//                }
//                super.userEventTriggered(ctx, evt);
//            }
//        });
        //防粘包
        pipeline.addLast(new LengthFieldBasedFrameDecoder(64, 9, 1, 2, 0));
        //处理上报数据
        pipeline.addLast(socketMsgHandler);
    }
}