package com.sw.job.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.quartz.Scheduler;
import com.sw.job.factory.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * @Description:  Quartz 配置
 * @Author:       allenyll
 * @Date:         2019/3/17 10:01 AM
 * @Version:      1.0
 */
@Configuration
public class QuartzConfig {

    @Value("${spring.datasource.ip:}")
    private String ip;

    @Value("${spring.datasource.username:}")
    private String username;

    @Value("${spring.datasource.password:}")
    private String password;

    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    @Autowired
    private JobFactory jobFactory;

    /**
     * 指定quartz.properties
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/config/quartz.properties"));

        // 在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * 配置Scheduler工厂
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setJobFactory(jobFactory);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://"+ ip +":3306/sweb?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&autoReconnectForPools=true");
        dataSource.setDriverClassName(driverClassName);
        dataSource.setMaxActive(50);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        factoryBean.setDataSource(dataSource);
        factoryBean.setOverwriteExistingJobs(true);
        try {
            factoryBean.setQuartzProperties(quartzProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  factoryBean;
    }

    /**
     * 注入Scheduler
     * @return
     */
    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

}





