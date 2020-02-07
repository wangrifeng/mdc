package com.app.mdc.service.system;

import com.app.mdc.model.system.User;
import com.app.mdc.model.system.UserLevel;
import com.baomidou.mybatisplus.service.IService;

import java.math.BigDecimal;
import java.util.Map;

public interface UserLevelService extends IService<UserLevel> {

    /**
     *  新增用户层级关系
     * @param recIds 推荐人id字符串集合
     * @param recedId 被推荐人id
     */
    void addLevelRelation(String recIds, String recedId);

    /**
     * 获取用户的被推荐人id
     * @param userId
     * @return
     */
    Map<Integer,  Map<String,Object>> selectRecedUserIds(Integer userId);

    /**
     * 获取用户的合约体量
     * @param userId
     * @return
     */
    BigDecimal getTotalSum(Integer userId);
}
