package com.app.mdc.serviceImpl.system;


import com.app.mdc.mapper.system.UserLevelMapper;
import com.app.mdc.model.system.*;
import com.app.mdc.service.system.UserLevelService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    @Override
    public void addLevelRelation(String recIds, String recedId) {
        if (StringUtils.isEmpty(recIds)) {
            return;
        }
        String[] split = recIds.split(",");
        List<UserLevel> userLevels = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            UserLevel userLevel = new UserLevel(split[i], recedId, split.length - i, new Date());
            userLevels.add(userLevel);
        }
        this.baseMapper.batchInsert(userLevels);
    }
}
