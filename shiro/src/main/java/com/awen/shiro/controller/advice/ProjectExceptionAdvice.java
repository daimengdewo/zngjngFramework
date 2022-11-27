package com.awen.shiro.controller.advice;

import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.common.Result;
import com.awen.shiro.exception.BusinessException;
import com.awen.shiro.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLSyntaxErrorException;

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

    //数据库查询失败
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public Result notSQLSyntaxErrorExceptionHandler(SQLSyntaxErrorException e) {
        log.error("SQLSyntaxErrorException");
        log.error(e.getLocalizedMessage());
        return new Result(Code.GET_ERR, null, Message.GET_ERR_MSG);
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
