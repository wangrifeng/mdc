package com.app.mdc.service.mdc;

import com.app.mdc.exception.BusinessException;
import com.app.mdc.model.mdc.Contract;
import com.app.mdc.model.mdc.UserContract;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

/**
 * 用户合约关系Service
 */
public interface UserContractService extends IService<UserContract> {

    /**
     * 查询用户合约卡详情
     * @param userId
     * @param type
     * @return
     */
    Contract selectContractByUserId(Integer userId,Integer type);

    /**
     * 新增用户合约
     * @param userId
     * @param contractId
     * @param number
     */
    void add(Integer userId, Integer contractId, Integer number) throws BusinessException;

    /**
     * 查询用户是否已拥有对应的合约
     * @param userId
     * @param type
     * @return
     */
    UserContract getUserContractByTypeAndUserId(Integer userId, Integer type);
}
