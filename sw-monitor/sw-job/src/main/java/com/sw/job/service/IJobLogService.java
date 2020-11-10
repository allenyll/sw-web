package com.sw.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.system.JobLog;
import org.quartz.JobDataMap;

/**
 * 调度任务日志记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-09 17:34:28
 */
public interface IJobLogService extends IService<JobLog> {

    /**
     * 保存日志
     * @param status
     * @param errMsg
     * @param time
     * @param jobDataMap
     */
    void saveJobLog(String status, String errMsg, long time, JobDataMap jobDataMap);
}
