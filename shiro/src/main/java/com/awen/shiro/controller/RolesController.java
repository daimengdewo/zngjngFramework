package com.awen.shiro.controller;

import com.awen.feign.common.Code;
import com.awen.feign.common.Message;
import com.awen.feign.common.Result;
import com.awen.feign.tool.FunctionMenu;
import com.awen.shiro.entity.Role;
import com.awen.shiro.service.RolesService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService rolesService;

    /**
     * 新增角色
     */
    @PostMapping("/add")
    public Result addRoles(@RequestBody Role role) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> flag = rolesService.RolesUtil(role, FunctionMenu.ADD);
        return new Result(flag.get() ? Code.SAVE_OK : Code.SAVE_ERR, null);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete")
    public Result deleteRoles(@RequestBody Role role) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> flag = rolesService.RolesUtil(role, FunctionMenu.DELETE);
        return new Result(flag.get() ? Code.DELETE_OK : Code.DELETE_ERR, null);
    }

    /**
     * 删除角色指定权限
     */
    @DeleteMapping("/deleteOne")
    public Result deleteRolesOne(@RequestBody Role role) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> flag = rolesService.RolesUtil(role, FunctionMenu.DELETE_ONE);
        return new Result(flag.get() ? Code.DELETE_OK : Code.DELETE_ERR, null);
    }

    /**
     * 修改角色
     */
    @PutMapping("/update")
    public Result updateRoles(@RequestBody Role role) throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> flag = rolesService.RolesUtil(role, FunctionMenu.UPDATE);
        return new Result(flag.get() ? Code.UPDATE_OK : Code.UPDATE_ERR, null);
    }

    /**
     * 角色列表分页查询
     */
    @GetMapping("/list")
    public Result listRoles(@RequestBody Map<String, Object> map) {
        try {
            Page<Role> employeePage =
                    rolesService.selectListRole((Integer) map.get("current"),
                            (Integer) map.get("size"));
            return new Result(employeePage != null ? Code.GET_OK : Code.GET_ERR, employeePage);
        } catch (NullPointerException e) {
            //参数不匹配
            return new Result(Code.SYSTEM_VALID_ERR, Message.SYSTEM_VALID_ERR_MSG);
        }
    }
}
