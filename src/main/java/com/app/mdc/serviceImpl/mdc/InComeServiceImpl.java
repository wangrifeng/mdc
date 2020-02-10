package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.mapper.mdc.InComeMapper;
import com.app.mdc.model.mdc.InCome;
import com.app.mdc.service.mdc.InComeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 收益servcieImpl
 */
@Service
public class InComeServiceImpl extends ServiceImpl<InComeMapper, InCome> implements InComeService {

    @Override
    public Map<Integer, Map<String,Object>> selectStaticIncomeGroupByLevel(Map<Integer, Map<String,Object>> levelIds, Date selDate, BigDecimal burnValue) {
        return this.baseMapper.selectStaticIncomeGroupByLevel(levelIds,selDate,burnValue.doubleValue());
    }

    @Override
    public Map<String, Object> getAdvanceShareSalary(Date selDate, Integer userId) {
        return this.baseMapper.getAdvanceShareSalary(selDate,userId);
    }

    @Override
    public BigDecimal getTotalSum(Integer userId, Date selDate, double burnValue) {
        return this.baseMapper.getTotalSum(userId,selDate,burnValue);
    }

    @Override
    public List<InCome> list(Integer userId) {
        return this.baseMapper.list(userId);
    }
}
