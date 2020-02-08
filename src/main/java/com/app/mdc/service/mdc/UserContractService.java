package com.app.mdc.service.mdc;

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
}
