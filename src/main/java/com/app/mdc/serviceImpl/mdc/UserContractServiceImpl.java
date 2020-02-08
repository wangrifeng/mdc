package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.exception.BusinessException;
import com.app.mdc.mapper.mdc.UserContractMapper;
import com.app.mdc.model.mdc.Contract;
import com.app.mdc.model.mdc.UserContract;
import com.app.mdc.model.system.User;
import com.app.mdc.service.mdc.ContractService;
import com.app.mdc.service.mdc.UserContractService;
import com.app.mdc.service.system.UserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户合约servcieImpl
 */
@Service
public class UserContractServiceImpl extends ServiceImpl<UserContractMapper, UserContract> implements UserContractService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserService userService;

    @Override
    public Contract selectContractByUserId(Integer userId, Integer type) {
        return this.baseMapper.selectContractByUserId(userId,type);
    }

    @Override
    public void add(Integer userId, Integer contractId, Integer number) throws BusinessException {
        //查询订购的合约详情
        Contract contract = contractService.selectById(contractId);
        if(contract == null){
            throw new BusinessException("系统合约查询异常");
        }
        Integer type = contract.getType();
        //查询用户是否已拥有对应的合约
        UserContract userContract = this.getUserContractByTypeAndUserId(userId,type);
        if(userContract != null){
            throw new BusinessException("该用户已存在对应类型的合约,请前往升级操作");
        }
        User user = userService.selectById(userId);
        userContract = new UserContract(userId, contractId, number, new Date(), user.getUserName());
        this.insert(userContract);
        //更新用户的等级
        userService.updateUserLevel(userId);
    }

    @Override
    public UserContract getUserContractByTypeAndUserId(Integer userId, Integer type) {
        return this.baseMapper.getUserContractByTypeAndUserId(userId,type);
    }
}
