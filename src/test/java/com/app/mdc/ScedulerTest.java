package com.app.mdc;

import com.app.mdc.schedule.QuartzUtils;
import com.app.mdc.schedule.ScheduleJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScedulerTest {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @Test
    public void testAddJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        ScheduleJob schedulerJob = new ScheduleJob();

        schedulerJob.setBeanClass("taskSchedulerService");
        schedulerJob.setMethodName("testTask");
        schedulerJob.setCronExpression("*/1 * * * * ?");
        schedulerJob.setJobId(UUID.randomUUID().toString());

        QuartzUtils.addJob(scheduler, schedulerJob);
        System.out.println("------------------------------");

    }

}
