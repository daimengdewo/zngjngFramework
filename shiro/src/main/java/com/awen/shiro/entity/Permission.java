package com.awen.shiro.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Permission {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限信息
     */
    private String info;

    /**
     * 权限描述
     */
    private String comment;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
}
