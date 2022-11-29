package com.awen.shiro.service.impl;

import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.entity.Permission;
import com.awen.shiro.mapper.PermissionMapper;
import com.awen.shiro.service.PermissionService;
import com.awen.shiro.tool.GeneralTools;
import com.awen.shiro.tool.MapperMenu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class PermissionImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private MapperMenu mapperMenu;
    @Autowired
    private GeneralTools generalTools;

    /**
     * 权限操作
     */
    @Override
    @Async
    public CompletableFuture<Boolean> PermissionUtil(Permission permission, FunctionMenu menu) {
        //操作结果标识
        boolean result = false;
        switch (menu) {
            case ADD:
                //权限重复判断
                if (generalTools.duplicatePermission(permission.getInfo()) > 0) {
                    break;
                }
                if (mapperMenu.getPermissionMapper().insert(permission) > 0) {
                    result = true;
                }
                break;
            case DELETE:
                //权限重复判断
                if (generalTools.duplicatePermission(permission.getId()) == 0) {
                    break;
                }
                if (generalTools.deletePermission(permission.getId()) > 0) {
                    result = true;
                }
                break;
            case UPDATE:
                //权限重复判断
                if (generalTools.duplicatePermission(permission.getId()) == 0) {
                    break;
                }
                if (generalTools.updatePermission(permission) > 0) {
                    result = true;
                }
                break;
        }
        return CompletableFuture.completedFuture(result);
    }

    /**
     * 分页查询角色列表
     */
    @Override
    public Page<Permission> selectListPermission(Integer current, Integer size) {
        //构造一个分页构造器
        Page<Permission> pageInfo = new Page<>(current, size);
        //条件构造器
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        //按更新时间排序
        wrapper.orderByDesc(Permission::getUpdateTime);
        return mapperMenu.getPermissionMapper().selectPage(pageInfo, wrapper);
    }
}
