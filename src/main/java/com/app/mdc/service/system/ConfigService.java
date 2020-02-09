package com.app.mdc.service.system;

import com.app.mdc.model.system.Config;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 参数配置表 服务类
 * </p>
 *
 * @author 
 * @since 2020-02-09
 */
public interface ConfigService extends IService<Config> {

    Config getByKey(String key);
}
