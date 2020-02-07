package com.app.mdc.service.mdc;

import com.app.mdc.model.mdc.Transaction;
import com.app.mdc.utils.viewbean.Page;
import com.app.mdc.utils.viewbean.ResponseResult;
import com.baomidou.mybatisplus.service.IService;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2020-02-05
 */
public interface TransactionService extends IService<Transaction> {

    /**
     * ETH钱包转账
     * @return ResponseResult
     */
    ResponseResult transETH(String fromWalletId,String toWalletId,String transferNumber,String payPassword,String userId,String toUserId,String walletType);

    /**
     * ETH钱包余额查询
     * @return ResponseResult
     */
    ResponseResult getETHBlance(Page page, Map<String,Object> params);

    /**
     * 充值
     * @param userId 用户id
     * @param toAddress 钱包address
     * @param investMoney 充值金额
     * @return ResponseResult
     */
    ResponseResult investUSDT(String userId,String toAddress,String investMoney);

    /**
     * 提现
     * @param userId 用户id
     * @param walletId 钱包id
     * @param toAddress 提现地址
     * @param cashOutMoney 提现金额
     * @return ResponseResult
     */
    ResponseResult cashOutUSDT(String userId,String walletId,String toAddress,String cashOutMoney) throws InterruptedException;

}