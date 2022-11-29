package com.awen.shiro.service;

import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.entity.Role;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface RolesService extends IService<Role> {
    //分页查询角色列表
    Page<Role> selectListRole(Integer current, Integer size);

    //角色操作
    CompletableFuture<Boolean> RolesUtil(Role role, FunctionMenu menu) throws ExecutionException, InterruptedException;
}
