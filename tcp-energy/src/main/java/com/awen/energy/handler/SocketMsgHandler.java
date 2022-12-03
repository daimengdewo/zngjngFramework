package com.awen.energy.handler;

import com.awen.energy.service.ChannelGroupService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class SocketMsgHandler extends SimpleChannelInboundHandler<String> {

    @Autowired
    private ChannelGroupService channelGroupService;

    //定义一个Channel 组，管理所有的channel
    //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 有客户端与服务器发生连接时执行此方法
     * 1.打印提示信息
     * 2.将客户端保存到 channelGroup 中
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有新的客户端与服务器发生连接。客户端地址：" + channel.remoteAddress());
        channelGroup.add(channel);
//        ChannelData channelData = new ChannelData();
//        channelData.setChannel(channel.remoteAddress().toString());
//        channelGroupService.channelGroupUtil(channelData, FunctionMenu.ADD);
    }

    /**
     * 当有客户端与服务器断开连接时执行此方法，此时会自动将此客户端从 channelGroup 中移除
     * 1.打印提示信息
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.err.println("有客户端与服务器断开连接。客户端地址：" + channel.remoteAddress());
    }

    /**
     * 表示channel 处于活动状态
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 处于活动状态");
    }

    /**
     * 表示channel 处于不活动状态
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 处于不活动状态");
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
//        ByteBuf buffer = ctx.alloc().buffer();
        System.out.println();
    }

    /**
     * 处理异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("发生异常。异常信息：{}", cause.getMessage());
        //关闭通道
        ctx.close();
    }
}
