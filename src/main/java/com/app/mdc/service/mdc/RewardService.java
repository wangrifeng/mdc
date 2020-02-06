package com.app.mdc.service.mdc;

import com.app.mdc.exception.BusinessException;
import com.app.mdc.model.mdc.Contract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 奖金计算
 */
public interface RewardService {

    void calculate(Integer userId, Map<Integer, Contract> contractCache, Date selDate) throws BusinessException;
}
