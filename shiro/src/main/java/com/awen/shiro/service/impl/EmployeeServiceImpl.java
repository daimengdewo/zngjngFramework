package com.awen.shiro.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.entity.Shiro;
import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.config.shiro.JwtUtil;
import com.awen.shiro.entity.Employee;
import com.awen.shiro.entity.JwtUser;
import com.awen.shiro.entity.Permission;
import com.awen.shiro.entity.Role;
import com.awen.shiro.exception.BusinessException;
import com.awen.shiro.mapper.EmployeeMapper;
import com.awen.shiro.service.EmployeeService;
import com.awen.shiro.tool.GeneralTools;
import com.awen.shiro.tool.MapperMenu;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private MapperMenu mapperMenu;
    @Autowired
    private GeneralTools generalTools;

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
        if (generalTools.duplicateEmployee(employee.getUsername(), employee.getPhone()) > 0) {
            throw new BusinessException(Code.SAVE_ERR, Message.TOO_MUCH_EMP_ERR_MSG);
        }
        //密码进行md5加盐处理
        String pass = SecureUtil.md5(employee.getPassword() + salt);
        //新建员工
        employee.setPassword(pass);
        mapperMenu.getEmployeeMapper().insert(employee);
        //分配角色
        if (generalTools.setEmployeeRole(employee.getId(), employee.getRole_id()) > 0) {
            return 1;
        } else {
            return 0;
        }
    }

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
                    break;
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
                    break;
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
                    break;
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
                    break;
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

    /**
     * 权限操作
     */
    @Override
    public Integer addPermission(Permission permission) {
        //查询重复权限
        if (generalTools.duplicatePermission(permission.getInfo()) > 0) {
            return 0;
        }
        return mapperMenu.getPermissionMapper().insert(permission);
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
        return mapperMenu.getEmployeeMapper().selectPage(pageInfo, wrapper);
    }

    /**
     * 1.统一登录
     * 2.查询账户info
     * 3.构造jwtUser
     */
    @Override
    public JwtUser jwtUserBuild(LambdaQueryWrapper<Employee> wrapper) {
        //查询员工信息
        Employee employee = mapperMenu.getEmployeeMapper().selectOne(wrapper);
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
            List<String> roles = mapperMenu.getEmployeeMapper().getUserRoleInfoMapper(employee.getUsername());
            //查询角色权限
            List<String> permission = mapperMenu.getEmployeeMapper().getUserPermissionInfoMapper(roles);
            //存入权限信息
            jwtUser.setRoles(permission);
            //返回jwtUser对象
            return jwtUser;
        }
        return null;
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
        return mapperMenu.getEmployeeMapper().updateById(employee);
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
        mapperMenu.getRedisTemplate().opsForValue().set(keyId, verifyCode, 120, TimeUnit.SECONDS);
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
        String verifyCode = mapperMenu.getRedisTemplate().opsForValue().get(keyId);
        //判断是否正确
        if (verifyCode != null) {
            flag = code.equals(verifyCode);
            //如果正确，则立即删除该验证码
            if (flag) {
                mapperMenu.getRedisTemplate().delete(keyId);
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
        //判断token是否有效
        boolean isCheck = JwtUtil.verifyTokenOfUser(token);
        //取出jwt携带的用户信息
        JwtUser info = JwtUtil.getInfo(token);
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
