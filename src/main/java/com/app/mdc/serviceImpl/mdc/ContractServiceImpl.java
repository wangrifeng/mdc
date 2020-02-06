package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.mapper.mdc.ContractMapper;
import com.app.mdc.model.mdc.Contract;
import com.app.mdc.service.mdc.ContractService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2020-02-05
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Override
    public Map<Integer, Contract> selectAllContract() {
        return this.baseMapper.selectAllContract();
    }
}
