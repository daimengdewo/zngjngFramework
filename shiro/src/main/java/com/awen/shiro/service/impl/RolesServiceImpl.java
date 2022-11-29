package com.awen.shiro.service.impl;

import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.entity.Role;
import com.awen.shiro.exception.BusinessException;
import com.awen.shiro.mapper.RoleMapper;
import com.awen.shiro.service.RolesService;
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
public class RolesServiceImpl extends ServiceImpl<RoleMapper, Role> implements RolesService {
    @Autowired
    private MapperMenu mapperMenu;
    @Autowired
    private GeneralTools generalTools;

    /**
     * 角色操作
     */
    @Override
    @Async
    public CompletableFuture<Boolean> RolesUtil(Role role, FunctionMenu menu) {
        //操作结果标识
        boolean result = false;
        switch (menu) {
            case ADD:
                //查询重复角色
                if (generalTools.duplicateRole(role.getName()) > 0
                        //查询是否存在该权限
                        && generalTools.duplicatePermission(role.getPermission_id()) == 0) {
                    throw new BusinessException(Code.SAVE_ERR, Message.ROLE_ERR_MSG);
                }
                //插入角色
                mapperMenu.getRoleMapper().insert(role);
                //建立角色权限映射关系
                if (generalTools.setRolePermission(role.getId(), role.getPermission_id()) > 0) {
                    result = true;
                }
                break;
            case DELETE:
                //角色是否存在
                if (generalTools.duplicateRole(role.getId()) == 0) {
                    throw new BusinessException(Code.SAVE_ERR, Message.ROLE_NOTNULL_ERR_MSG);
                }
                //删除角色
                if (generalTools.deleteRole(role.getId()) > 0
                        && generalTools.delRolePermission(role.getId()) > 0) {
                    result = true;
                }
                break;
            case DELETE_ONE:
                //角色是否存在
                if (generalTools.duplicateRole(role.getId()) == 0) {
                    throw new BusinessException(Code.SAVE_ERR, Message.ROLE_NOTNULL_ERR_MSG);
                }
                //删除角色指定权限
                if (generalTools.delRolePermission(role.getId(), role.getPermission_id()) > 1) {
                    result = true;
                } else {
                    throw new BusinessException(Code.DELETE_LAST_ROLE_ERR, Message.DELETE_LAST_ROLE_ERR_MSG);
                }
                break;
            case UPDATE:
                //角色是否存在
                if (generalTools.duplicateRole(role.getId()) == 0) {
                    throw new BusinessException(Code.SAVE_ERR, Message.ROLE_NOTNULL_ERR_MSG);
                }
                if (generalTools.updateRole(role) > 0) {
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
    public Page<Role> selectListRole(Integer current, Integer size) {
        //构造一个分页构造器
        Page<Role> pageInfo = new Page<>(current, size);
        //条件构造器
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //按更新时间排序
        wrapper.orderByDesc(Role::getUpdateTime);
        return mapperMenu.getRoleMapper().selectPage(pageInfo, wrapper);
    }
}
