package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.exception.BusinessException;
import com.app.mdc.mapper.mdc.UserContractMapper;
import com.app.mdc.model.mdc.Contract;
import com.app.mdc.model.mdc.UserContract;
import com.app.mdc.model.system.User;
import com.app.mdc.service.mdc.ContractService;
import com.app.mdc.service.mdc.UserContractService;
import com.app.mdc.service.system.UserService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        return this.baseMapper.selectContractByUserId(userId, type);
    }

    @Override
    @Transactional
    public void add(Integer userId, Integer contractId, Integer number) throws BusinessException {
        //查询订购的合约详情
        Contract contract = contractService.selectById(contractId);
        if (contract == null) {
            throw new BusinessException("系统合约查询异常");
        }
        Integer type = contract.getType();
        //查询用户是否已拥有对应的合约
        UserContract userContract = this.getUserContractByTypeAndUserId(userId, type);
        if (type == 1 && userContract != null) {
            throw new BusinessException("该用户已存在对应类型的合约,请前往升级操作");
        } else if (type == 2 && userContract != null && userContract.getNumber() + number >= 30) {
            throw new BusinessException("进阶卡最多只能购买30张,您已购买" + userContract.getNumber() + "张");
        }

        User user = userService.selectById(userId);

        if (type == 1) {
            //签约卡只能购买一张
            UserContract uc = new UserContract(userId, contractId, 1, new Date(), user.getUserName());
            this.insert(uc);

            //更新用户签约总额
            User u = new User();
            u.setId(userId.toString());
            u.setSelfSignTotalMoney(user.getSelfSignTotalMoney().add(contract.getAmount()));
            userService.updateById(u);
        } else {
            //进阶卡
            if (userContract == null) {
                UserContract uc = new UserContract(userId, contractId, number, new Date(), user.getUserName());
                this.insert(uc);
            } else {
                UserContract uc = new UserContract();
                uc.setId(userContract.getId());
                uc.setNumber(userContract.getNumber() + number);
                this.updateById(uc);
            }
            //更新用户进阶总额
            User u = new User();
            u.setId(userId.toString());
            u.setSelfAdvanceTotalMoney(user.getSelfAdvanceTotalMoney().add(contract.getAmount().multiply(new BigDecimal(number))));
            userService.updateById(u);
        }

        //更新用户的等级
        userService.updateUserLevel(userId);
    }

    @Override
    public UserContract getUserContractByTypeAndUserId(Integer userId, Integer type) {
        return this.baseMapper.getUserContractByTypeAndUserId(userId, type);
    }

    @Override
    public BigDecimal getUnionSignTotalMoney(Integer userId) {
        return this.baseMapper.getUnionSignTotalMoney(userId);
    }

    @Override
    public BigDecimal getUnionAdvanceTotalMoney(Integer userId) {
        return this.baseMapper.getUnionAdvanceTotalMoney(userId);
    }

    @Override
    @Transactional
    public void upgrade(Integer userId, Integer ucId, String payToken, Integer upgradeId) throws BusinessException {
        //TODO 判断支付是否成功 更新支付状态
        UserContract userContract = this.selectById(ucId);
        if (userContract == null) {
            throw new BusinessException("用户绑定合约查询失败");
        }
        Contract contract = contractService.selectById(userContract.getContractId());
        if (contract == null) {
            throw new BusinessException("合约信息查询失败");
        }
        //获取升级对应的合约信息
        Contract upgradeContract = contractService.selectById(upgradeId);
        if (upgradeContract == null) {
            throw new BusinessException("未查询到升级合约信息,无法升级");
        }

        //更新用户合约信息
        UserContract uc = new UserContract();
        uc.setId(ucId);
        uc.setContractId(upgradeContract.getId());
        this.updateById(uc);

        //更新用户账号余额
        User user = userService.selectById(userId);
        BigDecimal subtract = upgradeContract.getAmount().subtract(contract.getAmount());
        User u = new User();
        u.setId(userId.toString());
        u.setSelfSignTotalMoney(user.getSelfSignTotalMoney().add(subtract));
        userService.updateById(u);
    }

    @Override
    public BigDecimal getUpgradePriceDifference(Integer ucId, Integer upgradeId) throws BusinessException {
        UserContract userContract = this.selectById(ucId);
        if (userContract == null) {
            throw new BusinessException("用户绑定合约查询失败");
        }
        Contract contract = contractService.selectById(userContract.getContractId());
        if (contract == null) {
            throw new BusinessException("合约信息查询失败");
        }
        //获取升级对应的合约信息
        Contract upgradeContract = contractService.selectById(upgradeId);
        if (upgradeContract == null) {
            throw new BusinessException("未查询到升级合约信息,无法升级");
        }
        //计算差价
        BigDecimal subtract = upgradeContract.getPrice().subtract(contract.getPrice());
        return subtract;
    }

    @Override
    @Transactional
    public void rescind(Integer userId, Integer ucId) throws BusinessException {
        UserContract userContract = this.selectById(ucId);
        if (userContract == null) {
            throw new BusinessException("用户绑定合约查询失败");
        }
        Contract contract = contractService.selectById(userContract.getContractId());
        if (contract == null) {
            throw new BusinessException("合约信息查询失败");
        }
        if (contract.getType() == 2) {
            throw new BusinessException("只有签约卡才可以解约");
        }
        //计算违约金
        BigDecimal rescindMoney = contract.getAmount().multiply(new BigDecimal("0.05"));
        //更新用户余额
        User user = userService.selectById(userId);
        User u = new User();
        u.setId(userId.toString());
        u.setSelfSignTotalMoney(user.getSelfSignTotalMoney().subtract(rescindMoney));
        userService.updateById(u);

        //删除用户签约信息
        this.deleteById(ucId);
    }
}
