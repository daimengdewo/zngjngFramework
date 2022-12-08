package com.awen.energy.protocol.message;

import com.awen.energy.exception.BusinessException;
import com.awen.energy.protocol.StatusType;
import com.awen.energy.tool.DeviceTools;
import com.awen.feign.common.Code;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

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
        removeDeviceId(ctx);
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
        ArrayList<String> msgList = (ArrayList<String>) msg;

        //数据完整性校验
        if (!deviceTools.check(msgList)) {
            DeviceMessage.getChannelGroup().remove(ctx.channel());
            removeDeviceId(ctx);
            ctx.close();
            throw new BusinessException(Code.COMMON_ERR, "数据完整性校验失败！");
        }

        //报文设备id提取
        String deviceId = deviceTools.reverseDeviceId(msgList);
        DeviceMessage.getChannelMap().put(deviceId, ctx.channel());

        // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
        AttributeKey<String> key = AttributeKey.valueOf("deviceId");
        ctx.channel().attr(key).setIfAbsent(deviceId);

        String dataName = deviceTools.reverseDeviceDataName(msgList);
        Double data = deviceTools.reverseDeviceData(msgList);

        //判断上报的数据
        StatusType type = StatusType.getByType(dataName, StatusType.class);
        switch (Objects.requireNonNull(type)) {
            //单相电流
            case ONE_PHASE_CURRENT:
                System.out.println("单相电流：" + data / 1000);
                break;
            //三相电流
            case THREE_PHASE_CURRENT:
                System.out.println("三相电流：" + data);
                break;
            case VOLTAGE:
                System.out.println("电压：" + data / 10);
                break;
            case VALVE:
                System.out.println("闸门操作成功！");
                break;
        }
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生异常。异常信息：{}", cause.getMessage());
        // 删除通道
        DeviceMessage.getChannelGroup().remove(ctx.channel());
        removeDeviceId(ctx);
        ctx.close();
    }


    private void removeDeviceId(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("deviceId");
        String deviceId = ctx.channel().attr(key).get();
        if (deviceId != null) {
            DeviceMessage.getChannelMap().remove(deviceId);
        }
    }
}
