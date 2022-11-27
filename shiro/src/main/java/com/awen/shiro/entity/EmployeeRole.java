package com.awen.shiro.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EmployeeRole {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 员工id
     */
    private Long emp_id;

    /**
     * 角色id
     */
    private Long role_id;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
}
