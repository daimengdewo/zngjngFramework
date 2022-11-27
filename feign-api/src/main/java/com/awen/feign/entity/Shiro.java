package com.awen.feign.entity;

import lombok.Data;

import java.util.List;

@Data
public class Shiro {
    private Boolean isCheck;
    private Boolean isRoleCheck;
    private String username;
    private List<String> roles;
    private Long uid;
}
