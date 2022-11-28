package com.awen.shiro.tool;

import com.awen.shiro.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 常用代码抽象
 */
@Component
public class GeneralTools {
    @Autowired
    MapperMenu mapperMenu = new MapperMenu();

    //查询角色是否重复
    public Integer duplicateRole(String name) {
        //去重条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, name);
        return mapperMenu.getRoleMapper().selectCount(wrapper);
    }

    //查询角色是否存在
    public Integer duplicateRole(Long role_id) {
        //去重条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getId, role_id);
        return mapperMenu.getRoleMapper().selectCount(wrapper);
    }

    //角色删除
    public Integer deleteRole(Long role_id) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getId, role_id);
        return mapperMenu.getRoleMapper().delete(wrapper);
    }

    //角色更新
    public Integer updateRole(Role role) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, role.getName());
        return mapperMenu.getRoleMapper().update(role, wrapper);
    }

    //建立角色和权限映射
    public Integer setRolePermission(Long role_id, Long permission_id) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole_id(role_id);
        rolePermission.setPermission_id(permission_id);
        return mapperMenu.getRolePermissionMapper().insert(rolePermission);
    }

    //删除角色和权限映射
    public Integer delRolePermission(Long role_id) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRole_id, role_id);
        return mapperMenu.getRolePermissionMapper().delete(wrapper);
    }

    //删除角色和特定权限映射
    public Integer delRolePermission(Long role_id, Long permission_id) {
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRole_id, role_id)
                .eq(RolePermission::getPermission_id, permission_id);
        return mapperMenu.getRolePermissionMapper().delete(wrapper);
    }

    //查询权限是否存在
    public Integer duplicatePermission(Long permission_id) {
        //去重条件
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getId, permission_id);
        return mapperMenu.getPermissionMapper().selectCount(wrapper);
    }

    //查询权限是否重复
    public Integer duplicatePermission(String info) {
        //去重条件
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getInfo, info);
        return mapperMenu.getPermissionMapper().selectCount(wrapper);
    }

    //查询重复账户
    public Integer duplicateEmployee(String username, String phone) {
        //去重条件
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, username)
                .or().eq(Employee::getPhone, phone);
        return mapperMenu.getEmployeeMapper().selectCount(wrapper);
    }

    //建立账户和角色映射
    public Integer setEmployeeRole(Long employee_id, Long role_id) {
        EmployeeRole employeeRole = new EmployeeRole();
        employeeRole.setEmp_id(employee_id);
        employeeRole.setRole_id(role_id);
        return mapperMenu.getEmployeeRoleMapper().insert(employeeRole);
    }

    //删除账户和角色映射
    public Integer delEmployeeRole(Long emp_id) {
        LambdaQueryWrapper<EmployeeRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EmployeeRole::getEmp_id, emp_id);
        return mapperMenu.getEmployeeRoleMapper().delete(wrapper);
    }
}
