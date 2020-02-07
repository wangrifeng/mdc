package com.app.mdc.service.mdc;

import com.app.mdc.model.mdc.InCome;
import com.baomidou.mybatisplus.service.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 收益Service
 */
public interface InComeService extends IService<InCome> {

    /**
     * 查询所有被推荐人收益分代总和
     * @param levelIds
     * @param selDate
     * @param burnValue 烧伤值
     * @return
     */
    Map<Integer, Map<String,Object>> selectStaticIncomeGroupByLevel(Map<Integer, Map<String,Object>> levelIds, Date selDate, BigDecimal burnValue);
}
