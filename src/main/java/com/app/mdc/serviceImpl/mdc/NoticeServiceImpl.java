package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.mapper.mdc.NoticeMapper;
import com.app.mdc.model.mdc.Notice;
import com.app.mdc.service.mdc.NoticeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2020-02-05
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public Notice selectNewest() {
        return this.baseMapper.selectNewest();
    }
}
