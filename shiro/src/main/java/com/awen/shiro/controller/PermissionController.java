package com.awen.shiro.controller;

import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.common.Result;
import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.entity.Permission;
import com.awen.shiro.service.PermissionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 新增权限
     */
    @PostMapping("/add")
    public Result addPermission(@RequestBody Permission permission) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> flag = permissionService.PermissionUtil(permission, FunctionMenu.ADD);
        return new Result(flag.get() ? Code.SAVE_OK : Code.SAVE_ERR, null);
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/delete")
    public Result deletePermission(@RequestBody Permission permission) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> flag = permissionService.PermissionUtil(permission, FunctionMenu.DELETE);
        return new Result(flag.get() ? Code.DELETE_OK : Code.DELETE_ERR, null);
    }

    /**
     * 更新权限
     */
    @PutMapping("/update")
    public Result updatePermission(@RequestBody Permission permission) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> flag = permissionService.PermissionUtil(permission, FunctionMenu.UPDATE);
        return new Result(flag.get() ? Code.UPDATE_OK : Code.UPDATE_ERR, null);
    }

    /**
     * 权限列表分页查询
     */
    @GetMapping("/list")
    public Result listRoles(@RequestBody Map<String, Object> map) {
        try {
            Page<Permission> employeePage =
                    permissionService.selectListPermission((Integer) map.get("current"),
                            (Integer) map.get("size"));
            return new Result(employeePage != null ? Code.GET_OK : Code.GET_ERR, employeePage);
        } catch (NullPointerException e) {
            //参数不匹配
            return new Result(Code.SYSTEM_VALID_ERR, Message.SYSTEM_VALID_ERR_MSG);
        }
    }
}
