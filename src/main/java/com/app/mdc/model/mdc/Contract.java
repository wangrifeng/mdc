package com.app.mdc.model.mdc;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("sys_contract")
@Data
public class Contract {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("type")
    private Integer type;

    @TableField("level")
    private Integer level;

    @TableField("name")
    private String name;

    @TableField("price")
    private BigDecimal price;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("unit")
    private String unit;

    @TableField("introduce")
    private String introduce;

    @TableField("audio_path")
    private String audioPath;

    @TableField("income_rate")
    private BigDecimal incomeRate;

    @TableField("out_rate")
    private BigDecimal outRate;

    @TableField("create_time")
    private Date createTime;

    @TableField("create_by")
    private String createBy;

    @TableField("update_time")
    private Date updateTime;

    @TableField("update_by")
    private String updateBy;
}
