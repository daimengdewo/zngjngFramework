package com.awen.energy.handler;

import com.awen.energy.protocol.message.DeviceMessage;
import com.awen.energy.tool.DeviceTools;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class SocketMsgHandler extends SimpleChannelInboundHandler<String> {

    @Autowired
    private DeviceTools deviceTools;

    /**
     * 有客户端与服务器发生连接时执行此方法
     * 1.打印提示信息
     * 2.将客户端保存到 channelGroup 中
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有新的客户端与服务器发生连接。客户端地址：" + channel.remoteAddress());
        //保存channel
        DeviceMessage.getChannelGroup().add(channel);
    }

    /**
     * 当有客户端与服务器断开连接时执行此方法，此时会自动将此客户端从 channelGroup 中移除
     * 1.打印提示信息
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有客户端与服务器断开连接。客户端地址：" + channel.remoteAddress());
        DeviceMessage.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
    }

    /**
     * 读取到客户端发来的数据数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        //获取到当前channel
        Channel channel = ctx.channel();
        System.err.println("有客户端发来的数据。地址：" + channel.remoteAddress() + " 内容：" + msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String[] msgList = (String[]) msg;
        //报文设备id提取
        String deviceId = deviceTools.reverseDeviceId(msgList);
        DeviceMessage.getChannelMap().put(deviceId, ctx.channel());

        // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
        AttributeKey<String> key = AttributeKey.valueOf("deviceId");
        ctx.channel().attr(key).setIfAbsent(deviceId);

        // 回复消息
        ctx.channel().writeAndFlush(new byte[]{0x72, 0x67, 0x08, 0x01, 0x07, 0x31});
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生异常。异常信息：{}", cause.getMessage());
        // 删除通道
        DeviceMessage.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();
    }

    private void removeUserId(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("deviceId");
        String deviceId = ctx.channel().attr(key).get();
        DeviceMessage.getChannelMap().remove(deviceId);
    }
}
