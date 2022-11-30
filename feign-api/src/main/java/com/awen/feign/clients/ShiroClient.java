package com.awen.feign.clients;

import com.awen.feign.entity.Shiro;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("shiro")
public interface ShiroClient {
    @GetMapping("/employee/check/{token}/{roles}")
    Shiro check(@PathVariable("token") String token, @PathVariable("roles") String roles);

    @GetMapping("/employee/tokenError")
    void tokenError();

    @GetMapping("/employee/rolesError")
    void rolesError();
}
