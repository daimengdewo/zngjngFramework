package com.awen.energy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChannelData {
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * ip地址
     */
    @TableField(exist = false)
    private String address;

    /**
     * 设备id
     */
    private String deviceid;

    /**
     * 电流
     */
    private String current;

    /**
     * 电压
     */
    private String voltage;

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
