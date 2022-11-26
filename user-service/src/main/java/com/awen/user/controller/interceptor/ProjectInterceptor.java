package com.awen.user.controller.interceptor;

import com.awen.feign.entity.Shiro;
import com.awen.user.tool.ShiroCheck;
import com.awen.user.tool.ShiroTool;
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
            //获取token
            String token = request.getHeader("Authorization");
            token = token.replace("Bearer ", "");
            //反射获取注解
            ShiroCheck shiroCheck = handlerMethod.getMethod().getAnnotation(ShiroCheck.class);
            if (shiroCheck != null) {
                Shiro result = shiroTool.check(token);
                //判断权限检查结果
                if (!result.getIsCheck().equals("ture")) {
                    request.getRequestDispatcher("/user/tokenError").forward(request, response);
                    return false;
                }
                //获取权限列表
                System.out.println(result.getRoles());
                System.out.println(result);
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
