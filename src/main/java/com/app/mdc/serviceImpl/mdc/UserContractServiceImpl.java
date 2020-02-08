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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Transactional
    public void add(Integer userId, Integer contractId, Integer number) throws BusinessException {
        //查询订购的合约详情
        Contract contract = contractService.selectById(contractId);
        if(contract == null){
            throw new BusinessException("系统合约查询异常");
        }
        Integer type = contract.getType();
        //查询用户是否已拥有对应的合约
        UserContract userContract = this.getUserContractByTypeAndUserId(userId,type);
        if(type == 1 && userContract != null){
            throw new BusinessException("该用户已存在对应类型的合约,请前往升级操作");
        }else if(type == 2 && userContract!= null && userContract.getNumber() + number >=30 ){
            throw new BusinessException("进阶卡最多只能购买30张,您已购买" + userContract.getNumber() + "张");
        }

        User user = userService.selectById(userId);

        if(type == 1 ){
            //签约卡只能购买一张
            UserContract uc = new UserContract(userId, contractId, 1, new Date(), user.getUserName());
            this.insert(uc);
        }else{
            //进阶卡
            if(userContract == null){
                UserContract uc = new UserContract(userId, contractId, number, new Date(), user.getUserName());
                this.insert(uc);
            }else{
                UserContract uc = new UserContract();
                uc.setId(userContract.getId());
                uc.setNumber(userContract.getNumber() + number);
                this.updateById(uc);
            }
        }

        //更新用户的等级
        userService.updateUserLevel(userId);
    }

    @Override
    public UserContract getUserContractByTypeAndUserId(Integer userId, Integer type) {
        return this.baseMapper.getUserContractByTypeAndUserId(userId,type);
    }

    @Override
    public BigDecimal getUnionSignTotalMoney(Integer userId) {
        return this.baseMapper.getUnionSignTotalMoney(userId);
    }

    @Override
    public BigDecimal getUnionAdvanceTotalMoney(Integer userId) {
        return this.baseMapper.getUnionAdvanceTotalMoney(userId);
    }
}
