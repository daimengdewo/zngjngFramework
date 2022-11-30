package com.awen.shiro.controller;

import cn.hutool.crypto.SecureUtil;
import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.common.Result;
import com.awen.feign.entity.Shiro;
import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.config.shiro.JwtUtil;
import com.awen.shiro.entity.Employee;
import com.awen.shiro.entity.JwtUser;
import com.awen.shiro.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //盐
    @Value("${jwtProperties.Salt}")
    private String salt;

    /**
     * 1.登录校验
     * 2.异常捕获
     *
     * @return 统一Result
     */
    @PostMapping("/login")
    public Result employeeLogin(@RequestBody Map<String, Object> map) throws ExecutionException, InterruptedException {
        try {
            //校验图形验证码
            Boolean verify = employeeService.verifyImageCode(
                    map.get("keyId").toString(),
                    map.get("code").toString());
            //校验失败返回
            if (!verify) {
                return new Result(Code.GET_LOGIN_ERR, Message.CODE_ERR_MSG);
            }

            //构造wrapper对象
            LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
            //创建wrapper对象
            wrapper.eq(Employee::getUsername, map.get("username").toString())
                    .eq(Employee::getPassword, SecureUtil.md5(map.get("password").toString() + salt));
            //构造jwt对象
            JwtUser jwtUser = employeeService.jwtUserBuild(wrapper);
            //生成token
            Map<String, String> res = new HashMap<>();
            res.put("token", JwtUtil.createJwtTokenByUser(jwtUser));
            return new Result(Code.GET_LOGIN_OK, res);
        } catch (NullPointerException e) {
            //登录失败
            return new Result(Code.GET_USER_ERR, Message.LOGIN_ERR_MSG);
        }
    }

    /**
     * 手机登录
     */
    @PostMapping("/phone/login")
    public Result test(@RequestBody Employee employee) throws ExecutionException, InterruptedException {
        //构造wrapper对象
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //创建wrapper对象
        wrapper.eq(Employee::getPhone, employee.getPhone());
        //构造jwt对象
        JwtUser jwtUser = employeeService.jwtUserBuild(wrapper);
        //生成token
        Map<String, String> res = new HashMap<>();
        res.put("token", JwtUtil.createJwtTokenByUser(jwtUser));
        return new Result(Code.GET_LOGIN_OK, res);
    }

    /**
     * 新增员工
     */
    @PostMapping("/add")
    public Result addEmployee(@Validated @RequestBody Employee employee) {
        Boolean flag = employeeService.employeeUtil(employee, FunctionMenu.ADD);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, null);
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/delete")
    public Result deleteEmployee(@RequestBody Employee employee) {
        Boolean flag = employeeService.employeeUtil(employee, FunctionMenu.DELETE);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, null);
    }

    /**
     * 修改员工
     */
    @PutMapping("/update")
    public Result updateEmployee(@RequestBody Employee employee) {
        Boolean flag = employeeService.employeeUtil(employee, FunctionMenu.UPDATE);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, null);
    }

    /**
     * 账户禁用控制
     */
    @PutMapping("/disable")
    public Result employeeDisable(@RequestBody Employee employee) {
        Integer flag = employeeService.disable(employee);
        return new Result(flag > 0 ? Code.UPDATE_OK : Code.UPDATE_ERR, null);
    }

    /**
     * 员工列表分页查询
     */
    @GetMapping("/listEmployee")
    public Result listEmployee(@RequestBody Map<String, Object> map) {
        try {
            Page<Employee> employeePage =
                    employeeService.selectList((Integer) map.get("current"),
                            (Integer) map.get("size"), map.get("name").toString());
            return new Result(employeePage != null ? Code.GET_OK : Code.GET_ERR, employeePage);
        } catch (NullPointerException e) {
            //参数不匹配
            return new Result(Code.SYSTEM_VALID_ERR, Message.SYSTEM_VALID_ERR_MSG);
        }
    }

    /**
     * 响应验证码图片到前端，并返回验证码id
     */
    @GetMapping("/verify")
    public Result Verify(HttpServletResponse response) {
        Map<String, String> res;
        try {
            res = employeeService.verifyCreate(response);
        } catch (IOException e) {
            return new Result(Code.COMMON_ERR, null);
        }
        return new Result(Code.COMMON_OK, res);
    }

    /**
     * token校验和权限比对
     */
    @GetMapping("/check/{token}/{roles}")
    public Shiro checkToken(@PathVariable("token") String token, @PathVariable("roles") String roles) throws ExecutionException, InterruptedException {
        return employeeService.Check(token, roles).get();
    }
}
