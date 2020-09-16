package com.sw.common.util;

import com.sw.common.entity.SchedulerJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Description:  调度任务工具
 * @Author:       allenyll
 * @Date:         2019/3/17 10:37 AM
 * @Version:      1.0
 */
@Service
@Slf4j
public class JobUtil {

    @Resource
    Scheduler scheduler;

    public SchedulerJob jobToSchedulerJob(com.sw.common.entity.system.Job job){
        SchedulerJob schedulerJob = new SchedulerJob();
        schedulerJob.setClassName(job.getClassName());
        schedulerJob.setJobName(job.getJobName());
        schedulerJob.setCron(job.getCorn());
        schedulerJob.setDescription(job.getDescription());
        schedulerJob.setJobGroup(job.getJobGroup());
        schedulerJob.setJobStatus(job.getStatus());
        schedulerJob.setMethodName(job.getMethodName());
        schedulerJob.setSpringBean(job.getSpringBean());
        return schedulerJob;
    }

    /**
     * 添加调度任务
     * @param job
     */
    public void addJob(SchedulerJob job) {

        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(job.getClassName()).newInstance().getClass();

            // 根据类名和分组构建任务KEY
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();

            // 定义调度触发规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup())// 触发器key
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())).startNow().build();

            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 删除一个job
     *
     * @param schedulerJob
     * @throws SchedulerException
     */
    public void deleteJob(SchedulerJob schedulerJob) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(schedulerJob.getJobName(), schedulerJob.getJobGroup());
        scheduler.deleteJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param schedulerJob
     * @throws SchedulerException
     */
    public void updateJobCron(SchedulerJob schedulerJob) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(schedulerJob.getJobName(), schedulerJob.getJobGroup());

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulerJob.getCron());

        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(triggerKey, trigger);
    }

}

