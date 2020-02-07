package com.app.mdc.mapper.mdc;

import com.app.mdc.model.mdc.InCome;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface InComeMapper extends BaseMapper<InCome> {

    /**
     * 查询所有被推荐人收益分代总和
     * @param levelIds
     * @param selDate
     * @param burnValue 烧伤值
     * @return
     */
    @MapKey("level")
    Map<Integer, Map<String,Object>> selectStaticIncomeGroupByLevel(@Param("map") Map<Integer, Map<String, Object>> levelIds, @Param("selDate") Date selDate, @Param("burnValue") double burnValue);
}
