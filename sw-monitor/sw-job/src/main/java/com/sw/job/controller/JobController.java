package com.sw.job.controller;

import com.sw.client.annotion.CurrentUser;
import com.sw.common.constants.JobConstants;
import com.sw.common.entity.system.Job;
import com.sw.common.entity.system.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.MapUtil;
import com.sw.common.util.StringUtil;
import com.sw.client.controller.BaseController;
import com.sw.job.service.impl.JobServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Description:  调度任务接口
 * @Author:       allenyll
 * @Date:         2020/11/5 7:54 下午
 * @Version:      1.0
 */
@Api("调度任务接口相关接口")
@Slf4j
@Controller
@RequestMapping("job")
public class JobController extends BaseController<JobServiceImpl, Job> {

    @Autowired
    JobServiceImpl jobService;

    @ApiOperation("更新调度任务")
    @Override
    @ResponseBody
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public DataResponse update(@CurrentUser(isFull = true) User user, @RequestBody Job job) {
        if(job == null){
            return DataResponse.fail("参数为空，更新失败");
        }
        super.update(user, job);
        DataResponse dataResponse = super.get(job.getId());
        Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
        Job oldJob = (Job) data.get("obj");
        if(oldJob == null){
            return DataResponse.fail("调度任务不能为空!");
        }
        if(!oldJob.getCorn().equals(job.getCorn())){
            // 更新cron表达式
            try {
                jobService.updateCron(job);
            } catch (SchedulerException e) {
                return DataResponse.fail(e.getMessage());
            }
        }
        return DataResponse.success();
    }

    @ResponseBody
    @ApiOperation("更新调度任务状态")
    @RequestMapping("updateStatus")
    public DataResponse updateStatus(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> params) throws SchedulerException {
        log.info("开始更新调度任务状态");
        String flag = MapUtil.getMapValue(params, "flag");
        Long id = MapUtil.getLong(params, "id");
        Job job;
        if(StringUtil.isNotEmpty(id)){
            DataResponse dataResponse = super.get(id);
            Map<String, Object> data = (Map<String, Object>) dataResponse.get("data");
            job = (Job) data.get("obj");
            if(job == null){
                return DataResponse.fail("调度任务不能为空!");
            }
            params.put("job", job);
        }else{
            return DataResponse.fail("调度任务不能为空!");
        }

        jobService.updateStatus(params);

        if(JobConstants.JOB_START.equals(flag)){
            job.setStatus(JobConstants.JOB_START_DICT);
        }else{
            job.setStatus(JobConstants.JOB_STOP_DICT);
        }

        super.update(user, job);

        return DataResponse.success();
    }
}
