package com.sw.common.entity;

import lombok.Data;
import lombok.ToString;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.Serializable;

/**
 * @Description:  调度参数实体
 * @Author:       allenyll
 * @Date:         2019/3/17 10:52 AM
 * @Version:      1.0
 */
@Data
@ToString
public class SchedulerJob implements Serializable, Job {

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务表达式
     */
    private String cron;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    private String className;

    /**
     * Spring bean
     */
    private String springBean;

    /**
     * 任务调用的方法名
     */
    private String methodName;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
