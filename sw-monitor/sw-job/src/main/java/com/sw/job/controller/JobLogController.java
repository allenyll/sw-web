package com.sw.job.controller;

import com.sw.client.controller.BaseController;
import com.sw.job.service.impl.JobLogServiceImpl;
import com.sw.common.entity.system.JobLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 调度任务日志记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-09 17:34:28
 */
@Slf4j
@Api(value = "调度任务日志记录", tags = "调度任务日志记录")
@RestController
@RequestMapping("/jobLog")
public class JobLogController extends BaseController<JobLogServiceImpl,JobLog> {


}
