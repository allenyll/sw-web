package com.sw.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.system.Job;
import com.sw.common.entity.system.User;
import com.sw.common.util.Result;
import org.quartz.SchedulerException;

import java.util.Map;

/**
 * @Description:  调度任务<Job>服务接口
 * @Author:       allenyll
 * @Date:         2020/5/25 7:30 下午
 * @Version:      1.0
 */
public interface IJobService extends IService<Job> {

    /**
     * 删除job
     * @param user
     * @param job
     * @throws SchedulerException
     */
    void deleteJob(User user, Job job) throws SchedulerException;

    /**
     * 立即执行定时任务
     * @param user
     * @param params
     * @return
     */
    Result executeJob(User user, Map<String, Object> params) throws Exception ;
}
