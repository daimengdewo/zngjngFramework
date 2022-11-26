package com.awen.shiro.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.awen.shiro.common.Code;
import com.awen.shiro.common.Message;
import com.awen.shiro.entity.Employee;
import com.awen.shiro.entity.JwtUser;
import com.awen.shiro.exception.BusinessException;
import com.awen.shiro.mapper.EmployeeMapper;
import com.awen.shiro.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

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
    @Async
    public CompletableFuture<Integer> create(Employee employee) {
        //重复信息判断
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername())
                .or().eq(Employee::getPhone, employee.getPhone());
        Integer count = employeeMapper.selectCount(wrapper);
        //如果已经存在则抛出异常
        if (count > 0) {
            throw new BusinessException(Code.SAVE_ERR, Message.TOO_MUCH_EMP_ERR_MSG);
        }
        JwtUser jwtUser = (JwtUser) SecurityUtils.getSubject().getPrincipal();
        //密码进行md5加盐处理
        SimpleHash simpleHash = new SimpleHash("md5", employee.getPassword(), salt, 2);
        //构建对象
        employee.setPassword(simpleHash.toString());
        employee.setCreateUser(jwtUser.getUid());
        employee.setUpdateUser(jwtUser.getUid());
        //写入数据库并返回结果
        return CompletableFuture.completedFuture(employeeMapper.insert(employee));
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
}
