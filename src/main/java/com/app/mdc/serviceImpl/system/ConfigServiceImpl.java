package com.app.mdc.serviceImpl.system;

import com.app.mdc.model.system.Config;
import com.app.mdc.mapper.system.ConfigMapper;
import com.app.mdc.service.system.ConfigService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参数配置表 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-02-09
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    private final ConfigMapper configMapper;

    @Autowired
    public ConfigServiceImpl(ConfigMapper configMapper){
        this.configMapper = configMapper;
    }
    @Override
    public Config getByKey(String key) {
        EntityWrapper<Config> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("config_key",key);
        return configMapper.selectList(entityWrapper).get(0);
    }
}
