package com.awen.feign.clients;

import com.awen.feign.entity.Shiro;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("shiro")
public interface ShiroClient {
    @GetMapping("/employee/check/{token}")
    Shiro check(@PathVariable("token") String permission);
}
