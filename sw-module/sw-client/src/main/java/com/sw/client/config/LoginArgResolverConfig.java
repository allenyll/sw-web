package com.sw.client.config;

import com.sw.client.feign.CustomerFeignClient;
import com.sw.client.feign.SystemFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Description:
 * @Author:       allenyll
 * @Date:         2020/6/1 8:30 下午
 * @Version:      1.0
 */
public class LoginArgResolverConfig implements WebMvcConfigurer {
    @Lazy
    @Autowired
    private SystemFeignClient systemFeignClient;

    @Lazy
    @Autowired
    private CustomerFeignClient customerFeignClient;

    /**
     * Token参数解析
     *
     * @param argumentResolvers 解析类
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new CurrentUserMethodArgumentResolver(systemFeignClient, customerFeignClient));
    }
}
