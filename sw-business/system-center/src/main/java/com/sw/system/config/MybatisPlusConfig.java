package com.sw.system.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:  Mybatis配置
 * @Author:       allenyll
 * @Date:         2020/8/31 11:48 上午
 * @Version:      1.0
 */
@Configuration
@MapperScan({"com.sw.*"})
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}



