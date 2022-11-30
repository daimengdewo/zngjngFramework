package com.awen.feign.interceptor;

import com.awen.feign.clients.ShiroClient;
import com.awen.feign.entity.Shiro;
import com.awen.feign.tool.ShiroCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器配置
 */
@Component
public class ProjectInterceptor implements HandlerInterceptor {

    @Autowired
    private ShiroClient shiroClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HandlerMethod.class.equals(handler.getClass())) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //通过反射获取注解
            ShiroCheck shiroCheck = handlerMethod.getMethod().getAnnotation(ShiroCheck.class);
            if (shiroCheck != null) {
                //获取token
                String token = request.getHeader("Authorization").replace("Bearer ", "");
                //远程调用安全模块 做token合法性验证和权限校验
                Shiro result = shiroClient.check(token, shiroCheck.roles());//注解保存的接口权限要求，类似shiro的@RequiresRoles
                //如果失败，重定向到token校验异常返回
                if (!result.getIsCheck()) {
                    request.getRequestDispatcher("/global/tokenError").forward(request, response);
                    return result.getIsCheck();
                }
                //如果没有权限，重定向到权限校验异常返回
                if (!result.getIsRoleCheck()) {
                    request.getRequestDispatcher("/global/rolesError").forward(request, response);
                    return result.getIsRoleCheck();
                }
            }
        }
        return true;
    }
}
