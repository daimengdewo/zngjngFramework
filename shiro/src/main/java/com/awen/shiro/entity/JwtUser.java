package com.awen.shiro.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JwtUser implements Serializable {
    private String username;
    private List<String> roles;
    private Long uid;
}
