package com.awen.guest.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
@TableName("t_guest")
public class Guest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统管理员账号
     */
    private String sys_username;

    /**
     * 住户微信id
     */
    @NotNull(message = "住户微信id不能为空")
    private String guest_unionid;

    //住户公众号id
    @NotNull(message = "住户公众号id不能为空")
    private String guest_openid;

    /**
     * 住户系统id
     */
    @TableId(value = "guest_id", type = IdType.AUTO)
    private Long guest_id;

    /**
     * 住户名称
     */
    @NotNull(message = "住户名称不能为空")
    private String guest_name;

    /**
     * 住户手机号码
     */
    @NotNull(message = "住户手机号码不能为空")
    private String guest_phone;

    /**
     * 住户身份证号码
     */
    @NotNull(message = "住户身份证号码不能为空")
    private String guest_card;

    //住户状态
    private Integer guest_type;

    /**
     * 住户人脸url
     */
    private String guest_face;

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


}
