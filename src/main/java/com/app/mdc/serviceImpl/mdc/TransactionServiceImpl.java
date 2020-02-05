package com.app.mdc.serviceImpl.mdc;

import com.app.mdc.model.mdc.Transaction;
import com.app.mdc.mapper.mdc.TransactionMapper;
import com.app.mdc.service.mdc.TransactionService;
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
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

}
