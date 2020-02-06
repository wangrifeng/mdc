package com.app.mdc.mapper.system;

import com.app.mdc.model.system.UserLevel;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserLevelMapper extends BaseMapper<UserLevel> {

    /**
     * 批量新增用户层级关系
     * @param userLevels
     */
    void batchInsert(@Param("list") List<UserLevel> userLevels);
}
