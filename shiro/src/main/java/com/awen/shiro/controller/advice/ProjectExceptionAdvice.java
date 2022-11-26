package com.awen.shiro.controller.advice;

import com.awen.shiro.common.Code;
import com.awen.shiro.common.Message;
import com.awen.shiro.common.Result;
import com.awen.shiro.exception.BusinessException;
import com.awen.shiro.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ProjectExceptionAdvice {
    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException exception) {
        //记录日志
        //通知运维
        //通知开发
        return new Result(exception.getCode(), null, exception.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException exception) {
        return new Result(exception.getCode(), exception.getMessage());
    }

    // 身份验证错误
    @ExceptionHandler(AuthenticationException.class)
    public Result authenticationExceptionHandler(AuthenticationException e) {
        log.error("AuthenticationException");
        log.error(e.getLocalizedMessage());
        return new Result(Code.GET_USER_ERR, null, Message.USER_ERR_MSG);
    }

    //权限验证错误
    @ExceptionHandler(UnauthorizedException.class)
    public Result unauthorizedExceptionHandler(UnauthorizedException e) {
        log.error("unauthorizedExceptionHandler");
        log.error(e.getLocalizedMessage());
        return new Result(Code.GET_AUTHORIZED_ERR, null, Message.AUTHORIZED_ERR_MSG);
    }

    //对应路径不存在
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result noHandlerFoundExceptionHandler(NoHandlerFoundException e) {
        log.error("noHandlerFoundExceptionHandler");
        log.error(e.getLocalizedMessage());
        return new Result(Code.GET_HANDLER_ERR, null, Message.HANDLER_ERR_MSG);
    }

    //参数类型不匹配
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result notValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("notValidException");
        log.error(e.getLocalizedMessage());
        return new Result(Code.SYSTEM_VALID_ERR, null, Message.SYSTEM_VALID_ERR_MSG);
    }

    //通用异常
//    @ExceptionHandler(Exception.class)
//    public Result doException(Exception e) {
//        //记录日志
//        //通知运维
//        //通知开发
//        return new Result(Code.SYSTEM_UNKNOW_ERR, "系统繁忙，请稍后再试！");
//    }
}
