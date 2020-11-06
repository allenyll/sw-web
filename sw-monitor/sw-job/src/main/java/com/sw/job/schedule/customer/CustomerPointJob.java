//package com.sw.job.schedule.customer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * @Description:  会员积分过期任务
// * @Author:       allenyll
// * @Date:         2019/3/20 9:31 PM
// * @Version:      1.0
// */
//@Slf4j
//public class CustomerPointJob implements Job {
//
//    @Autowired
//    CustomerPointDetailServiceImpl customerPointDetailService;
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//
//        EntityWrapper<CustomerPointDetail> wrapper = new EntityWrapper<>();
//        List<CustomerPointDetail> list = customerPointDetailService.selectList(wrapper);
//    }
//}
