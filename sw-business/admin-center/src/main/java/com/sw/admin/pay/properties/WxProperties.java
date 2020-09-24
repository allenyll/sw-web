package com.sw.admin.pay.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
* @Title: WeChatProperties
* @Package com.sw.wechat.properties
* @Description: TODO
* @author yu.leilei
* @date 2018/10/19 11:44
* @version V1.0
*/
@Data
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "auth.wechat")
public class WxProperties {

    @Value("${sessionHost}")
    private String sessionHost;

    @Value("${appId}")
    private String appId;

    @Value("${appSecret}")
    private String appSecret;

    @Value("${grantType}")
    private String grantType;

    @Value("${systemWebUrl}")
    private String systemWebUrl;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${mchId}")
    private String mchId;

    @Value("${key}")
    private String key;

    @Value("${orderUrl}")
    private String orderUrl;

    @Value("${signType}")
    private String signType;

    @Value("${tradeType}")
    private String tradeType;

}
