package com.sw.job.schedule.coupon;

import com.sw.client.feign.AdminFeignClient;
import com.sw.common.constants.dict.CouponDict;
import com.sw.common.constants.dict.IsOrNoDict;
import com.sw.common.entity.market.Coupon;
import com.sw.common.entity.market.CouponDetail;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DateUtil;
import com.sw.job.service.IJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:  优惠券过期
 * @Author:       allenyll
 * @Date:         2019-07-15 12:07
 * @Version:      1.0
 */
@Slf4j
@Component
public class CouponJob implements Job {

    @Autowired
    AdminFeignClient adminFeignClient;

    @Autowired
    IJobLogService jobLogService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Long startTime = System.currentTimeMillis();
        String errMsg = "";
        String status = IsOrNoDict.YES.getCode();
        try {
            Map<String, Object> param = new HashMap<>();
            List<Coupon> list = adminFeignClient.getCouponList(param);
            if(CollectionUtil.isNotEmpty(list)){
                for(Coupon coupon:list){
                    String time = DateUtil.getCurrentDate();
                    // 优惠券已过期
                    if(time.compareTo(coupon.getEndTime()) > 0){
                        param.put("COUPON_ID", coupon.getId());
                        List<CouponDetail> couponDetails = adminFeignClient.getCouponDetailList(param);
                        if(CollectionUtil.isNotEmpty(couponDetails)){
                            for(CouponDetail couponDetail:couponDetails){
                                if(couponDetail.getUseStatus().equals(CouponDict.UN_USE.getCode())){
                                    couponDetail.setUseStatus(CouponDict.EXPIRE.getCode());
                                    adminFeignClient.updateById(couponDetail);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
             log.info("优惠券调度执行失败！", e.getMessage());
             errMsg = e.getMessage();
             status = IsOrNoDict.NO.getCode();
        }
        Long endTime = System.currentTimeMillis();
        jobLogService.saveJobLog(status, errMsg, endTime-startTime, jobExecutionContext.getJobDetail().getJobDataMap());
    }



}
