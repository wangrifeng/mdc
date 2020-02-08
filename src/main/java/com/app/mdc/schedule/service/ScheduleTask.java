package com.app.mdc.schedule.service;

import com.app.mdc.mapper.mdc.TransactionMapper;
import com.app.mdc.model.mdc.Transaction;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private TransactionMapper transactionMapper;
    @Scheduled(cron = "0 */10 * * * ?")
    private void invest(){
        EntityWrapper<Transaction> transactionEntityWrapper = new EntityWrapper<>();
        transactionEntityWrapper.eq("transaction_type","0").eq("transaction_status","0");
        List<Transaction> transactions = transactionMapper.selectList(transactionEntityWrapper);

    }

}
