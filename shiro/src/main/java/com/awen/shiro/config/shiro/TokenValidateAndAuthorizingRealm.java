package com.awen.shiro.config.shiro;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.awen.shiro.entity.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class TokenValidateAndAuthorizingRealm extends AuthorizingRealm {

//    public TokenValidateAndAuthorizingRealm() {
//        //CredentialsMatcher，自定义匹配策略（即验证jwt token的策略）
//        super(new CredentialsMatcher() {
//            @Override
//            public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
//                log.info("doCredentialsMatch token合法性验证");
//                BearerToken bearerToken = (BearerToken) authenticationToken;
//                String bearerTokenString = bearerToken.getToken();
//                return JwtUtil.verifyTokenOfUser(bearerTokenString);
//            }
//        });
//    }

    @Override//权限管理
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("doGetAuthorizationInfo 权限验证");

        JwtUser jwtUser = (JwtUser) SecurityUtils.getSubject().getPrincipal();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(jwtUser.getRoles());//roles跟着user走，放到token里。
        //获取传入权限列表
        Set<String> stringPermissions = new HashSet<>(jwtUser.getRoles());
        simpleAuthorizationInfo.addStringPermissions(stringPermissions);
        return simpleAuthorizationInfo;
    }

    @Override
    public String getName() {
        return "TokenValidateAndAuthorizingRealm";
    }

    @Override
    public Class getAuthenticationTokenClass() {
        //设置由本realm处理的token类型。BearerToken是在filter里自动装配的。
        return BearerToken.class;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        //预留
        return super.supports(token);
    }

    @Override
    //装配用户信息，供Matcher调用
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException, TokenExpiredException {
        log.debug("doGetAuthenticationInfo 将token装载成用户信息");

        BearerToken bearerToken = (BearerToken) authenticationToken;
        String bearerTokenString = bearerToken.getToken();

        JwtUser jwtUser = JwtUtil.recreateUserFromToken(bearerTokenString);//带着用户名,roles

        /*Constructor that takes in an account's identifying principal(s) and its corresponding credentials that verify the principals.*/
        //        这个返回值是造Subject用的，返回值供createSubject使用
        return new SimpleAuthenticationInfo(jwtUser, bearerTokenString, this.getName());
    }

}