package com.awen.shiro.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    @Size(min = 6, max = 15, message = "账户长度必须在6-15之间")
    private String username;

    @NotNull
    @Size(max = 10, message = "员工姓名长度必须小于10")
    private String name;

    @NotNull
    @Size(min = 6, max = 15, message = "密码长度必须在6-15之间")
    private String password;

    @NotNull
    @Size(max = 11, message = "手机号码长度必须小于11")
    private String phone;

    @NotNull
    @Size(min = 15, max = 18, message = "手机号码的长度必须在15-18之间")
    private String idNumber;

    /**
     * 账号是否禁用
     */
    private Integer status;

    /**
     * 分配角色id
     */
    @TableField(exist = false)
    private Long role_id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
