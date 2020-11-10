package com.sw.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.constants.JobConstants;
import com.sw.common.constants.dict.StatusDict;
import com.sw.common.entity.SchedulerJob;
import com.sw.common.entity.system.Job;
import com.sw.common.entity.system.User;
import com.sw.common.util.DateUtil;
import com.sw.common.util.JobUtil;
import com.sw.common.util.MapUtil;
import com.sw.common.util.Result;
import com.sw.job.mapper.JobMapper;
import com.sw.job.service.IJobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 调度任务表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-17 12:07:34
 */
@Service("jobService")
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

    @Resource
    JobMapper jobMapper;

    @Autowired
    JobUtil jobUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    /**
     * 初始化所有启用的调度
     */
    public void initSchedule() {

        QueryWrapper<Job> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("STATUS", StatusDict.START.getCode());

        List<Job> list = jobMapper.selectList(wrapper);

        if(!CollectionUtils.isEmpty(list)){
            for(Job job:list){
                SchedulerJob schedulerJob = jobUtil.jobToSchedulerJob(job);
                jobUtil.addJob(schedulerJob);
            }
        }
    }

    /**
     * 更新调度状态
     * @param params
     * @throws SchedulerException
     */
    public void updateStatus(Map<String, Object> params) throws SchedulerException {
        String flag = MapUtil.getMapValue(params, "flag");
        Job job = (Job) params.get("job");
        SchedulerJob schedulerJob = jobUtil.jobToSchedulerJob(job);
        if(schedulerJob == null){
            return;
        }
        if(JobConstants.JOB_START.equals(flag)){
           schedulerJob.setJobStatus(JobConstants.JOB_START_DICT);
           jobUtil.addJob(schedulerJob);
        } else {
            jobUtil.deleteJob(schedulerJob);
        }
    }

    /**
     * 更新调度任务cron表达式
     * @param job
     */
    public void updateCron(Job job) throws SchedulerException {
        jobUtil.updateJobCron(jobUtil.jobToSchedulerJob(job));
    }

    @Override
    public void deleteJob(User user, Job job) throws SchedulerException {
        SchedulerJob schedulerJob = jobUtil.jobToSchedulerJob(job);
        if(schedulerJob != null){
            if (job.getStatus().equals(StatusDict.START.getCode())) {
                jobUtil.deleteJob(schedulerJob);
            }
            QueryWrapper<Job> delWrapper = new QueryWrapper<>();
            delWrapper.eq("id", job.getId());
            job.setIsDelete(1);
            job.setUpdateTime(DateUtil.getCurrentDateTime());
            job.setUpdateUser(user.getId());
            jobMapper.update(job, delWrapper);
        }
    }

    @Override
    public Result executeJob(User user, Map<String, Object> params) throws Exception {
        String id = MapUtil.getString(params, "id");
        Job job = jobMapper.selectById(id);
        if (job != null) {
            jobUtil.executeJob(jobUtil.jobToSchedulerJob(job));
        }
        return new Result();
    }
}
