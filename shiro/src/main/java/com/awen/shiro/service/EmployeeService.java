package com.awen.shiro.service;

import com.awen.feign.entity.Shiro;
import com.awen.shiro.entity.Employee;
import com.awen.shiro.entity.JwtUser;
import com.awen.shiro.entity.Permission;
import com.awen.shiro.entity.Role;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EmployeeService extends IService<Employee> {
    //新增员工
    Integer createEmployee(Employee employee);

    //新增角色
    Integer addRoles(Role role);

    //新增权限
    Integer addPermission(Permission permission);

    //分页查询
    Page<Employee> selectList(Integer current, Integer size, String name);

    //创建jwtUser对象
    CompletableFuture<JwtUser> jwtUserBuild(LambdaQueryWrapper<Employee> wrapper);

    //禁用控制
    Integer Disable(Employee employee);

    //生成验证码
    Map<String, String> VerifyCreate(HttpServletResponse response) throws IOException;

    //校验图形验证码
    Boolean VerifyImageCode(String keyId, String code);

    //校验器
    CompletableFuture<Shiro> Check(String token, String roles);
}
