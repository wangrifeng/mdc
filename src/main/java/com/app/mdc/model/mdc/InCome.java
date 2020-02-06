package com.app.mdc.model.mdc;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@TableName("mdc_income")
@Data
public class InCome {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("salary")
    private BigDecimal salary;

    @TableField("type")
    private Integer type;

    @TableField("unit")
    private String unit;

    @TableField("contract_id")
    private Integer contractId;

    @TableField("remark")
    private String remark;

    @TableField("amount")
    private BigDecimal amount;

    @TableField("rate")
    private BigDecimal rate;

    @TableField("sel_date")
    private Date selDate;

    @TableField("create_time")
    private Date createTime;

    @TableField("create_by")
    private String createBy;

    public InCome() {
    }

    public InCome(BigDecimal salary, String unit, Integer contractId, int type, String remark, BigDecimal amount, BigDecimal incomeRate,Date selDate, Date createTime) {
        this.salary = salary;
        this.unit = unit;
        this.contractId = contractId;
        this.type = type;
        this.remark = remark;
        this.amount = amount;
        this.rate = incomeRate;
        this.selDate = selDate;
        this.createTime = createTime;
    }
}
