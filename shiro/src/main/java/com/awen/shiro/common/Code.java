package com.awen.shiro.common;

/**
 * 响应代码
 * 与持久层相关的代码规则为：200x|x
 * 与查询相关的代码规则为：2xx4|x
 * 与账户相关的代码规则为：2x14|x
 * 与SYSTEM相关的代码规则为：500x|x
 * 与业务相关的代码规则为：600x|x
 */
public class Code {
    public static final Integer COMMON_OK = 10001;
    public static final Integer COMMON_ERR = 10000;

    /**
     * 新增成功
     */
    public static final Integer SAVE_OK = 20011;
    public static final Integer DELETE_OK = 20021;
    public static final Integer UPDATE_OK = 20031;
    /**
     * 查询成功
     */
    public static final Integer GET_OK = 20041;
    /**
     * 登录成功
     */
    public static final Integer GET_LOGIN_OK = 20141;

    public static final Integer SAVE_ERR = 20010;
    public static final Integer DELETE_ERR = 20020;
    public static final Integer UPDATE_ERR = 20030;
    /**
     * 查询失败
     */
    public static final Integer GET_ERR = 20040;

    /**
     * 登录失败
     */
    public static final Integer GET_LOGIN_ERR = 20140;
    /**
     * 身份验证错误
     */
    public static final Integer GET_USER_ERR = 21140;
    /**
     * 权限验证错误
     */
    public static final Integer GET_AUTHORIZED_ERR = 22140;
    /**
     * 对应路径不存在
     */
    public static final Integer GET_HANDLER_ERR = 23140;
    /**
     * 账户被禁用
     */
    public static final Integer GET_STATUS_ERR = 24140;

    /**
     * 系统级异常
     */
    public static final Integer SYSTEM_ERR = 50010;
    /**
     * 系统响应超时
     */
    public static final Integer SYSTEM_TIMEOUT_ERR = 50110;
    /**
     * 系统未知异常
     */
    public static final Integer SYSTEM_UNKNOW_ERR = 59990;
    /**
     * 参数不匹配
     */
    public static final Integer SYSTEM_VALID_ERR = 50210;

    /**
     * 业务级异常
     */
    public static final Integer BUSINESS_ERR = 60010;
    /**
     * token验证失败
     */
    public static final Integer TOKEN_ERR = 60110;
}
