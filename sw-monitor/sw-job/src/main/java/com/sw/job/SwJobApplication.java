package com.sw.job;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.sw.client.annotion.EnableLoginArgResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableLoginArgResolver
@EnableFeignClients(basePackages = {"com.sw.client.feign"})
@ComponentScan("com.sw")
@EnableDiscoveryClient
@EnableSwagger2
@SpringBootApplication
public class SwJobApplication {

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
        SpringApplication.run(SwJobApplication.class, args);
    }

}
