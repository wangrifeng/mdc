package com.app.mdc.model.mdc;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2020-02-05
 */
@TableName("mdc_transaction")
public class Transaction extends Model<Transaction> {

    private static final long serialVersionUID = 1L;

    /**
     * 交易id
     */
    @TableId(value = "transaction_id", type = IdType.AUTO)
    private Integer transactionId;
    /**
     * 支出用户id
     */
    private Integer fromUserId;
    /**
     * 支出钱包id
     */
    private Integer fromWalletId;
    /**
     * 收取用户id
     */
    private Integer toUserId;
    /**
     * 收取钱包id
     */
    private Integer toWalletId;
    /**
     * 交易金额
     */
    private BigDecimal fromAmount;

    private String fromWalletType;

    private BigDecimal toAmount;

    private String toWalletType;

    private BigDecimal feeAmount;
    /**
     * 交易类型（0-转账）
     */
    private String transactionType;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易说明
     */
    private String remark;

    private String transactionStatus;

    private String transactionHash;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getFromWalletId() {
        return fromWalletId;
    }

    public void setFromWalletId(Integer fromWalletId) {
        this.fromWalletId = fromWalletId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getToWalletId() {
        return toWalletId;
    }

    public void setToWalletId(Integer toWalletId) {
        this.toWalletId = toWalletId;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(BigDecimal fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getFromWalletType() {
        return fromWalletType;
    }

    public void setFromWalletType(String fromWalletType) {
        this.fromWalletType = fromWalletType;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public void setToAmount(BigDecimal toAmount) {
        this.toAmount = toAmount;
    }

    public String getToWalletType() {
        return toWalletType;
    }

    public void setToWalletType(String toWalletType) {
        this.toWalletType = toWalletType;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    @Override
    protected Serializable pkVal() {
        return this.transactionId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
        ", transactionId=" + transactionId +
        ", fromUserId=" + fromUserId +
        ", fromWalletId=" + fromWalletId +
        ", toUserId=" + toUserId +
        ", toWalletId=" + toWalletId +
        ", transactionType=" + transactionType +
        ", createTime=" + createTime +
        ", remark=" + remark +
        "}";
    }
}
