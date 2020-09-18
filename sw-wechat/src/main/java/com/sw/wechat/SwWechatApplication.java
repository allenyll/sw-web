package com.sw.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan("com.sw")
@EnableDiscoveryClient
@EnableFeignClients("com.sw")
@SpringBootApplication()
public class SwWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwWechatApplication.class, args);
    }

}
