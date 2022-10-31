package com.awen.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * feign配置类
 */
public class DefaultFeignConfiguration {

    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.NONE;
    }
}
