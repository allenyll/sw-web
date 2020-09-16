package com.sw.gateway.config;

import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:  ZUUL配置
 * @Author:       allenyll
 * @Date:         2020/8/31 7:12 下午
 * @Version:      1.0
 */
@Configuration
public class ZuulConfig {

    /**
     * 自定义 serviceId 和路由之间的相互映射
     * @return
     */
    @Bean
    public PatternServiceRouteMapper serviceRouteMapper() {
        return new PatternServiceRouteMapper(
                "(?<project>^.+)-(?<subProject>.+$)",
                "${project}/${subProject}");
    }

}
