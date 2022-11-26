package com.awen.shiro.entity;

import lombok.Data;

import java.util.List;

@Data
public class Shiro {
    private String isCheck;
    private String username;
    private List<String> roles;
    private Long uid;
}
