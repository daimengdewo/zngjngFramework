package com.awen.guest;

import com.awen.feign.clients.ShiroClient;
import com.awen.feign.config.DefaultFeignConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.awen.user.mapper")
@SpringBootApplication
@EnableFeignClients(clients = {ShiroClient.class}, defaultConfiguration = DefaultFeignConfiguration.class)
public class GuestApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuestApplication.class, args);
    }
}
