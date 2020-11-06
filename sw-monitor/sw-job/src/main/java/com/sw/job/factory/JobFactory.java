package com.sw.job.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:  Job实例工厂
 *        解决spring注入问题，如果使用默认会导致spring的@Autowired 无法注入问题
 * @Author:       allenyll
 * @Date:         2019/3/17 9:51 AM
 * @Version:      1.0
 */
@Component
public class JobFactory extends AdaptableJobFactory {

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 调用父类进行实例化
        Object job = super.createJobInstance(bundle);
        // 进行注入
        capableBeanFactory.autowireBean(job);
        return job;
    }
}
