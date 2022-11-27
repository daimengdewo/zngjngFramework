package com.awen.feign.interceptor;

import com.awen.feign.entity.Shiro;
import com.awen.feign.tool.ShiroCheck;
import com.awen.feign.tool.ShiroTool;
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
    private ShiroTool shiroTool;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HandlerMethod.class.equals(handler.getClass())) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //反射获取注解
            ShiroCheck shiroCheck = handlerMethod.getMethod().getAnnotation(ShiroCheck.class);
            if (shiroCheck != null) {
                //获取token
                String token = request.getHeader("Authorization").replace("Bearer ", "");
                //校验
                Shiro result = shiroTool.check(token, shiroCheck.roles());
                //判断权限检查结果
                if (!result.getIsCheck()) {
                    request.getRequestDispatcher("/user/tokenError").forward(request, response);
                    return result.getIsCheck();
                }
                //根据权限列表判断
                if (!result.getIsRoleCheck()) {
                    request.getRequestDispatcher("/user/rolesError").forward(request, response);
                    return result.getIsRoleCheck();
                }
            }
        }
        return true;
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//    }
}
