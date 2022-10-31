package com.awen.order;

import com.awen.feign.clients.UserClient;
import com.awen.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.awen.order.mapper")
@SpringBootApplication
//配置feign统一api
@EnableFeignClients(clients = {UserClient.class}, defaultConfiguration = DefaultFeignConfiguration.class)
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}