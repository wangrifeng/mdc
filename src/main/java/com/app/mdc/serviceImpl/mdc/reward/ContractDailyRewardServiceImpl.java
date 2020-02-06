package com.app.mdc.serviceImpl.mdc.reward;

import com.alibaba.fastjson.JSON;
import com.app.mdc.model.mdc.Contract;
import com.app.mdc.model.mdc.InCome;
import com.app.mdc.model.mdc.UserContract;
import com.app.mdc.service.mdc.InComeService;
import com.app.mdc.service.mdc.RewardService;
import com.app.mdc.service.mdc.UserContractService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 合同日收益
 */
@Service
public class ContractDailyRewardServiceImpl implements RewardService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserContractService userContractService;

    @Autowired
    private InComeService inComeService;

    @Override
    @Transactional
    public void calculate(Integer userId, Map<Integer, Contract> contractCache, Date selDate) {
        //查询该用户绑定的合约信息
        EntityWrapper<UserContract> userContractEntityWrapper = new EntityWrapper<>();
        userContractEntityWrapper
                .eq("del_flag", "0")
                .eq("user_id", userId);
        List<UserContract> userContracts = userContractService.selectList(userContractEntityWrapper);
        if (userContracts.size() == 0) {
            return;
        }
        for (UserContract userContract : userContracts) {
            Integer contractId = userContract.getContractId();
            Contract contract = contractCache.get(contractId);
            if (contract == null) {
                logger.info("用户Id" + userId + "获取合同id" + contractId + "信息失败,请及时检查");
                continue;
            }
            BigDecimal amount = contract.getAmount();
            String unit = contract.getUnit();
            BigDecimal incomeRate = contract.getIncomeRate();
            BigDecimal salary = amount.multiply(incomeRate);
            String remark = "合约信息为" + JSON.toJSONString(contract) + ";";

            BigDecimal newReceivedIncome = userContract.getReceivedIncome().add(salary);
            //合约 判断是否2倍出局
            boolean isOut = false;

            //2倍合约
            BigDecimal thirdMutify = contract.getAmount().multiply(contract.getOutRate());
            if (newReceivedIncome.compareTo(thirdMutify) == 1) {
                //重新计算薪水
                salary = thirdMutify.subtract(userContract.getReceivedIncome());
                newReceivedIncome = userContract.getReceivedIncome().add(salary);
                //出局
                isOut = true;
                remark += "合约已超过2倍,出局";
            }

            //薪水入库
            InCome inCome = new InCome(salary, unit, contract.getId(), 1, remark, contract.getAmount(), contract.getIncomeRate(), selDate, new Date());
            inComeService.insert(inCome);

            //更新合约收益状态
            UserContract uc = new UserContract();
            uc.setId(userContract.getId());
            uc.setReceivedIncome(newReceivedIncome);
            userContractService.updateById(uc);

            if (isOut) {
                userContractService.deleteById(userContract.getId());
            }
        }
    }
}
