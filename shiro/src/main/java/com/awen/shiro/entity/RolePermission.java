package com.awen.shiro.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RolePermission {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 角色id
     */
    private Long role_id;

    /**
     * 权限id
     */
    private Long permission_id;

    /**
     * 乐观锁
     */
    @TableField(fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
