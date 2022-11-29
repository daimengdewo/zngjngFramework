package com.awen.shiro.service;

import com.awen.feign.entity.Shiro;
import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.entity.Employee;
import com.awen.shiro.entity.JwtUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EmployeeService extends IService<Employee> {
    //新增员工
    Boolean employeeUtil(Employee employee, FunctionMenu menu);

    //分页查询员工信息
    Page<Employee> selectList(Integer current, Integer size, String name);

    //创建jwtUser对象
    JwtUser jwtUserBuild(LambdaQueryWrapper<Employee> wrapper);

    //禁用控制
    Integer disable(Employee employee);

    //生成验证码
    Map<String, String> verifyCreate(HttpServletResponse response) throws IOException;

    //校验图形验证码
    Boolean verifyImageCode(String keyId, String code);

    //校验器
    CompletableFuture<Shiro> Check(String token, String roles);
}
