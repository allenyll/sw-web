package com.sw.wechat.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Description:  swagger 配置
 * @Author:       allenyll
 * @Date:         2020/5/10 11:15 下午
 * @Version:      1.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .select()
                .apis(RequestHandlerSelectors.basePackage("")) // 需要生成文档的包的位置
                .paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildApiInf() {
        return new ApiInfoBuilder()
                .title("SYSTEM_WEB Interface doc")
                .description("Zuul+Swagger2 Build RESTFul APIs")
                .termsOfServiceUrl("http://www.allenyll.com")
                .contact(new Contact("allenyll", "http://www.allenyll.com", ""))
                .version("1.0")
                .build();
    }
}
