package com.awen.energy;

import com.awen.energy.protocol.listener.SocketServerListener;
import com.awen.feign.clients.ShiroClient;
import com.awen.feign.config.DefaultFeignConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {ShiroClient.class}, defaultConfiguration = DefaultFeignConfiguration.class)
public class EnergyApplication implements CommandLineRunner {

    @Autowired
    private SocketServerListener socketServerListener;

    public static void main(String[] args) {
        SpringApplication.run(EnergyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        socketServerListener.start();
    }
}
