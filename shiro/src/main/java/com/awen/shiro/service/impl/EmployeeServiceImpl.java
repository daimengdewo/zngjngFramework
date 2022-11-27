package com.awen.shiro.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.entity.Shiro;
import com.awen.shiro.config.shiro.JwtUtil;
import com.awen.shiro.entity.*;
import com.awen.shiro.exception.BusinessException;
import com.awen.shiro.mapper.*;
import com.awen.shiro.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //盐
    @Value("${jwtProperties.Salt}")
    private String salt;

    /**
     * 新增员工
     * 1.获取密码并加盐加密
     * 3.构建对象
     * 4.存入数据库
     * 5.返回结果
     */
    @Override
    public Integer createEmployee(Employee employee) {
        //重复信息判断
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername())
                .or().eq(Employee::getPhone, employee.getPhone());
        Integer count = employeeMapper.selectCount(wrapper);
        //如果已经存在则抛出异常
        if (count > 0) {
            throw new BusinessException(Code.SAVE_ERR, Message.TOO_MUCH_EMP_ERR_MSG);
        }
        //密码进行md5加盐处理
        String pass = SecureUtil.md5(employee.getPassword() + salt);
        //员工对象
        employee.setPassword(pass);
        int empFlag = employeeMapper.insert(employee);
        //分配角色
        EmployeeRole employeeRole = new EmployeeRole();
        employeeRole.setEmp_id(employee.getId());
        employeeRole.setRole_id(employee.getRole_id());
        //必须全部插入成功才返回1
        int empRoleFlag = employeeRoleMapper.insert(employeeRole);
        if (empRoleFlag > 0 && empFlag > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 新增角色并分配权限
     */
    @Override
    public Integer addRoles(Role role) {
        //去重条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, role.getName());
        //查询重复角色
        Integer count = roleMapper.selectCount(wrapper);
        if (count > 0) {
            return 0;
        }
        //查询是否存在该权限
        LambdaQueryWrapper<Permission> P_wrapper = new LambdaQueryWrapper<>();
        P_wrapper.eq(Permission::getId, role.getPermission_id());
        Integer P_count = permissionMapper.selectCount(P_wrapper);
        if (P_count <= 0) {
            return 0;
        }
        //插入角色
        int roleFlag = roleMapper.insert(role);
        //角色权限映射关系
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole_id(role.getId());
        rolePermission.setPermission_id(role.getPermission_id());
        int rolePermissionFlag = rolePermissionMapper.insert(rolePermission);
        //必须全部插入成功才返回1
        if (roleFlag > 0 && rolePermissionFlag > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 新增权限
     */
    @Override
    public Integer addPermission(Permission permission) {
        //去重
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getInfo, permission.getInfo());
        //查询重复权限
        Integer count = permissionMapper.selectCount(wrapper);
        if (count > 0) {
            return 0;
        }
        return permissionMapper.insert(permission);
    }

    /**
     * 员工账户信息分页查询
     *
     * @param current 页码
     * @param size    每页大小
     * @param name    员工姓名
     */
    @Override
    public Page<Employee> selectList(Integer current, Integer size, String name) {
        //构造一个分页构造器
        Page<Employee> pageInfo = new Page<>(current, size);
        //条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //模糊匹配姓名
        wrapper.like(name != null, Employee::getName, name);
        //按更新时间排序
        wrapper.orderByDesc(Employee::getUpdateTime);
        return employeeMapper.selectPage(pageInfo, wrapper);
    }

    /**
     * 1.统一登录
     * 2.查询账户info
     * 3.构造jwtUser
     */
    @Override
    @Async
    public CompletableFuture<JwtUser> jwtUserBuild(LambdaQueryWrapper<Employee> wrapper) {
        //查询员工信息
        Employee employee = employeeMapper.selectOne(wrapper);
        //非空判断
        if (employee != null) {
            //判断账户状态是否正常
            if (employee.getStatus() == 0) {
                throw new BusinessException(Code.GET_STATUS_ERR, Message.STATUS_ERR_MSG);
            }
            //构造jwtUser对象
            JwtUser jwtUser = new JwtUser();
            jwtUser.setUsername(employee.getUsername());
            jwtUser.setUid(employee.getId());
            //查询账户角色
            List<String> roles = employeeMapper.getUserRoleInfoMapper(employee.getUsername());
            //查询角色权限
            List<String> permission = employeeMapper.getUserPermissionInfoMapper(roles);
            //存入权限信息
            jwtUser.setRoles(permission);
            //返回jwtUser对象
            return CompletableFuture.completedFuture(jwtUser);
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 员工账户禁用控制
     *
     * @param employee 员工对象
     * @return flag判断是否成功
     */
    @Override
    public Integer Disable(Employee employee) {
        //修改账户状态
        employee.setStatus(employee.getStatus());
        //执行更新操作
        return employeeMapper.updateById(employee);
    }

    /**
     * 生成验证码图片并保存到redis缓存中
     */
    @Override
    public Map<String, String> VerifyCreate(HttpServletResponse response) {
        //定义哈希表
        Map<String, String> res = new HashMap<>();
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(150, 40, 5, 4);
        //图形验证码转base64
        String imageBase64 = captcha.getImageBase64Data();
        res.put("image", imageBase64);
        //获取验证码中的文字内容
        String verifyCode = captcha.getCode();
        //生成keyId
        String keyId = new Date().getTime() + RandomUtil.randomString(6);
        res.put("keyId", keyId);
        //保存该验证码120秒
        redisTemplate.opsForValue().set(keyId, verifyCode, 120, TimeUnit.SECONDS);
        return res;
    }

    /**
     * 校验图形验证码
     *
     * @param keyId redis keyId
     * @return flag判断结果
     */
    @Override
    public Boolean VerifyImageCode(String keyId, String code) {
        boolean flag = false;
        //从redis取出验证码
        String verifyCode = redisTemplate.opsForValue().get(keyId);
        //判断是否正确
        if (verifyCode != null) {
            flag = code.equals(verifyCode);
            //如果正确，则立即删除该验证码
            if (flag) {
                redisTemplate.delete(keyId);
            }
        }
        return flag;
    }

    /**
     * 校验器
     */
    @Override
    @Async
    public CompletableFuture<Shiro> Check(String token, String roles) {
        Shiro shiro = new Shiro();
        boolean isCheck = JwtUtil.verifyTokenOfUser(token);
        JwtUser info = JwtUtil.getInfo(token);
        //判断token是否有效
        if (info != null && isCheck) {
            //构造shiro对象
            shiro.setIsCheck(true);
            shiro.setUsername(info.getUsername());
            shiro.setRoles(info.getRoles());
            shiro.setUid(info.getUid());
        } else {
            shiro.setIsCheck(false);
        }
        //权限校验
        if (roles != null && shiro.getRoles() != null) {
            Set<String> roleSet = new HashSet<>(shiro.getRoles());
            Boolean contains = roleSet.contains(roles);
            shiro.setIsRoleCheck(contains);
        } else {
            shiro.setIsRoleCheck(false);
        }
        return CompletableFuture.completedFuture(shiro);
    }


}
