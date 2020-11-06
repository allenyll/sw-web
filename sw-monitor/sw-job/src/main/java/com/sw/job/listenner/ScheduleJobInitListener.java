package com.sw.job.listenner;

import com.sw.common.util.JobUtil;
import com.sw.job.service.impl.JobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description:  调度任务监听
 * @Author:       allenyll
 * @Date:         2019/3/18 5:50 PM
 * @Version:      1.0
 */
@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

	@Autowired
	JobServiceImpl jobService;

	@Autowired
    JobUtil jobUtil;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			jobService.initSchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
