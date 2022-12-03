package com.awen.energy.protocol.listener;

import com.awen.energy.entity.EnergyImConfig;
import com.awen.energy.protocol.initializer.SocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@Slf4j
@Component
public class SocketServerListener {
    /**
     * 创建bootstrap
     */
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    /**
     * BOSS
     */
    EventLoopGroup boss = new NioEventLoopGroup();
    /**
     * Worker
     */
    EventLoopGroup work = new NioEventLoopGroup();
    @Resource
    private EnergyImConfig imConfig;
    @Autowired
    private SocketServerInitializer socketServerInitializer;

    /**
     * 开启服务线程
     */
    public void start() {
        // 从配置文件中(application.yml)获取服务端监听端口号
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100);
        try {
            serverBootstrap.childHandler(socketServerInitializer);
            log.info("netty服务器在[{}]端口启动监听", imConfig.getPort());
            ChannelFuture f = serverBootstrap.bind(imConfig.getPort()).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("[出现异常] 释放资源");
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        log.info("关闭服务器....");
        //优雅退出
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }
}