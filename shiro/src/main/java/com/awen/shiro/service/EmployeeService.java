package com.awen.shiro.service;

import com.awen.feign.entity.Shiro;
import com.awen.feign.tool.FunctionMenu;
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
import java.util.concurrent.ExecutionException;

public interface EmployeeService extends IService<Employee> {
    //新增员工
    Integer createEmployee(Employee employee);

//    //删除员工
//    Integer deleteEmployee(Employee employee);
//
//    //修改员工
//    Integer updateEmployee(Employee employee);

    //分页查询员工信息
    Page<Employee> selectList(Integer current, Integer size, String name);

    //分页查询角色列表
    Page<Role> selectListRole(Integer current, Integer size);

    //角色操作
    CompletableFuture<Boolean> RolesUtil(Role role, FunctionMenu menu) throws ExecutionException, InterruptedException;

    //新增权限
    Integer addPermission(Permission permission);

    //创建jwtUser对象
    JwtUser jwtUserBuild(LambdaQueryWrapper<Employee> wrapper);

    //禁用控制
    Integer Disable(Employee employee);

    //生成验证码
    Map<String, String> VerifyCreate(HttpServletResponse response) throws IOException;

    //校验图形验证码
    Boolean VerifyImageCode(String keyId, String code);

    //校验器
    CompletableFuture<Shiro> Check(String token, String roles);
}
