package com.sw.admin.pay.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
@Configuration
public class WxProperties {

    @Value("${auth.wechat.sessionHost:}")
    private String sessionHost;

    @Value("${auth.wechat.appId:}")
    private String appId;

    @Value("${auth.wechat.appSecret:}")
    private String appSecret;

    @Value("${auth.wechat.grantType:}")
    private String grantType;

    @Value("${auth.wechat.systemWebUrl:}")
    private String systemWebUrl;

    @Value("${auth.wechat.username:}")
    private String username;

    @Value("${auth.wechat.password:}")
    private String password;

    @Value("${auth.wechat.mchId:}")
    private String mchId;

    @Value("${auth.wechat.key:}")
    private String key;

    @Value("${auth.wechat.orderUrl:}")
    private String orderUrl;

    @Value("${auth.wechat.orderQuery:}")
    private String orderQuery;

    @Value("${refundUrl:}")
    private String refundUrl;

    @Value("${signType:}")
    private String signType;

    @Value("${tradeType:}")
    private String tradeType;

}
