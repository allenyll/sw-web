package com.sw.system;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sw.client.annotion.EnableLoginArgResolver;
import com.sw.common.util.SnowflakeIdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description:  系统管理启动类
 * @Author:       allenyll
 * @Date:         2020/8/31 7:46 下午
 * @Version:      1.0
 */
@EnableLoginArgResolver
@EnableFeignClients("com.sw")
@ComponentScan("com.sw")
@EnableDiscoveryClient
@EnableSwagger2
@SpringBootApplication
public class SystemCenterApplication {

    /**
     * 解决Jackson导致Long型数据精度丢失问题
     *
     * @return
     */
    @Bean("jackson2ObjectMapperBuilderCustomizer")
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        Jackson2ObjectMapperBuilderCustomizer customizer =
                jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
                        .serializerByType(Long.class, ToStringSerializer.instance)
                        .serializerByType(Long.TYPE, ToStringSerializer.instance);
        return customizer;
    }

    public static void main(String[] args) {
        SpringApplication.run(SystemCenterApplication.class, args);
    }

}
