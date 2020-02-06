package com.app.mdc.service.system;

import com.app.mdc.model.system.UserLevel;
import com.baomidou.mybatisplus.service.IService;

public interface UserLevelService extends IService<UserLevel> {

    /**
     *  新增用户层级关系
     * @param recIds 推荐人id字符串集合
     * @param recedId 被推荐人id
     */
    void addLevelRelation(String recIds, String recedId);
}
