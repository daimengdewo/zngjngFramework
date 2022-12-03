package com.awen.guest.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author awen
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_guest_account")
public class GuestAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 住户系统id
     */
    private Long guest_id;

    /**
     * 账户余额
     */
    private Double balance;

    //欠费金额
    private Double arrearage;

    /**
     * 记录创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime create_time;

    /**
     * 记录更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modify_time;

    /**
     * 线程锁（乐观锁）
     */
    @Version
    private Integer version;


}
