package com.sw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description:  网管服务启动
 * @Author:       allenyll
 * @Date:         2020/8/31 7:25 下午
 * @Version:      1.0
 */
@ComponentScan("com.sw")
@EnableFeignClients(basePackages = {"com.sw.client.feign"})
@EnableSwagger2
@EnableZuulProxy
@SpringBootApplication
public class SwGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwGatewayApplication.class, args);
    }

}
