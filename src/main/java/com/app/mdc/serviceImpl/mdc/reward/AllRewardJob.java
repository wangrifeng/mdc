package com.app.mdc.serviceImpl.mdc.reward;

import com.app.mdc.exception.BusinessException;
import com.app.mdc.model.mdc.Contract;
import com.app.mdc.service.mdc.ContractService;
import com.app.mdc.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 计算所有收益
 */
@Component
public class AllRewardJob {

    @Autowired
    private UserService userService;
    @Autowired
    private ContractDailyRewardServiceImpl contractDailyRewardService;
    @Autowired
    private ContractService contractService;

    private static final Executor executor = Executors.newFixedThreadPool(10);

    public void execute() throws BusinessException {
        //找出所有用户ids
        List<Integer> userIds = userService.findAllUserIds();
        if (userIds.size() == 0) {
            return;
        }
        //查询所有的合约收益信息
        Map<Integer, Contract> contractCache = contractService.selectAllContract();
        if (contractCache.size() == 0) {
            throw new BusinessException("系统异常,合约信息查询失败");
        }

        //计算结算时间
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.DAY_OF_MONTH, -1); //当前时间减去一天，即一天前的时间
        Date selDate = instance.getTime();

        for (Integer userId : userIds) {
//            executor.execute(new Thread(() -> contractDailyRewardService.calculate(userId, contractCache,selDate)));
            contractDailyRewardService.calculate(userId, contractCache,selDate);
        }
    }
}
