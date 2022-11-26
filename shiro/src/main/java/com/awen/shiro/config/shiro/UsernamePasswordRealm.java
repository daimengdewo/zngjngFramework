package com.awen.shiro.config.shiro;

import com.awen.shiro.entity.Employee;
import com.awen.shiro.entity.JwtUser;
import com.awen.shiro.mapper.EmployeeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

//Username Password Realm，用户名密码登陆专用Realm
@Slf4j
@Component
public class UsernamePasswordRealm extends AuthenticatingRealm {

    @Autowired
    private EmployeeMapper employeeMapper;

    //盐
    @Value("${jwtProperties.Salt}")
    private String salt;

    /*构造器里配置Matcher*/
    public UsernamePasswordRealm() {
        super();
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);//密码保存策略一致，2次md5加密
        this.setCredentialsMatcher(hashedCredentialsMatcher);
    }

    /**
     * 通过该方法来判断是否由本realm来处理login请求
     * <p>
     * 调用{@code doGetAuthenticationInfo(AuthenticationToken)}之前会shiro会调用{@code supper.supports(AuthenticationToken)}
     * 来判断该realm是否支持对应类型的AuthenticationToken,如果相匹配则会走此realm
     */
    @Override
    public Class getAuthenticationTokenClass() {
        return UsernamePasswordToken.class;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //继承但啥都不做
        //预留
        return super.supports(token);
    }

    //自定义登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户名
        String username = authenticationToken.getPrincipal().toString();
        //数据库查询该用户
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee employee = employeeMapper.selectOne(queryWrapper);
        //封装对象返回
        if (employee != null) {
            //封装用户信息
            JwtUser jwtUser = new JwtUser();
            jwtUser.setUsername(username);
            //权限信息
            ArrayList<String> list = new ArrayList<>();
            jwtUser.setRoles(list);
            return new SimpleAuthenticationInfo(
                    //用户信息
                    jwtUser,
                    //密码
                    employee.getPassword(),
                    //盐值
                    ByteSource.Util.bytes(salt),
                    getName()
            );
        }
        return null;
    }
}
