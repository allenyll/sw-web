package com.sw.job.service.impl;

import com.sw.common.util.DateUtil;
import com.sw.common.util.SnowflakeIdWorker;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.system.JobLog;
import com.sw.job.mapper.JobLogMapper;
import com.sw.job.service.IJobLogService;

/**
 * 调度任务日志记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-09 17:34:28
 */
@Service("jobLogService")
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper,JobLog> implements IJobLogService{

    @Autowired
    JobLogMapper jobLogMapper;

    @Override
    public void saveJobLog(String status, String errMsg, long time, JobDataMap jobDataMap) {
        Long id = jobDataMap.getLong("id");
        String name = jobDataMap.getString("jobName");
        String group = jobDataMap.getString("jobGroup");
        JobLog jobLog = new JobLog();
        jobLog.setId(SnowflakeIdWorker.generateId());
        jobLog.setJobId(id);
        jobLog.setJobName(name);
        jobLog.setJobGroup(group);
        jobLog.setStatus(status);
        jobLog.setMessage(errMsg);
        jobLog.setTime(time);
        jobLog.setIsDelete(0);
        jobLog.setAddTime(DateUtil.getCurrentDateTime());
        jobLogMapper.insert(jobLog);
    }
}
