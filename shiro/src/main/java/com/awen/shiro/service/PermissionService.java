package com.awen.shiro.service;

import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.entity.Permission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.CompletableFuture;

public interface PermissionService extends IService<Permission> {
    //新增权限
    CompletableFuture<Boolean> PermissionUtil(Permission permission, FunctionMenu menu);

    //分页查询权限列表
    Page<Permission> selectListPermission(Integer current, Integer size);
}
