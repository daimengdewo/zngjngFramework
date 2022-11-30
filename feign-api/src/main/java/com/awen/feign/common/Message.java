package com.awen.feign.common;

public class Message {
    public static final String LOGIN_ERR_MSG = "登录失败！";
    public static final String USER_ERR_MSG = "身份验证错误！";
    public static final String AUTHORIZED_ERR_MSG = "权限验证错误！";
    public static final String HANDLER_ERR_MSG = "对应路径不存在！";
    public static final String TOKEN_ERR_MSG = "token失效或异常，请重新登录！";
    public static final String STATUS_ERR_MSG = "该账户已被禁用！";
    public static final String SYSTEM_VALID_ERR_MSG = "参数不匹配！";
    public static final String TOO_MUCH_EMP_ERR_MSG = "该用户已存在！(用户名与手机号必须唯一)";
    public static final String CODE_ERR_MSG = "验证码错误或已经失效！";
    public static final String GET_ERR_MSG = "数据库查询失败！";
    public static final String DELETE_LAST_ROLE_ERR_MSG = "无法删除角色唯一权限！请尝试直接移除角色！";
    public static final String EMPLOYEE_ERR_MSG = "目标用户不存在！";
    public static final String PERMISSION_ERR_MSG = "目标权限不存在！";
    public static final String PERMISSION_NOTNULL_ERR_MSG = "目标权限已存在！";
    public static final String ROLE_NOTNULL_ERR_MSG = "目标角色已存在！";
    public static final String ROLE_ERR_MSG = "目标角色不存在！";
    public static final String EMPLOYEE_PHONE_NOTNULL_ERR_MSG = "该手机号已被使用！";
}
