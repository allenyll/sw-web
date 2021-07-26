//package com.sw.admin.config;
//
//import com.github.binarywang.wxpay.config.WxPayConfig;
//import com.github.binarywang.wxpay.service.WxPayService;
//import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
//import com.sw.admin.pay.properties.WxProperties;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;    
//
//// @Configuration
//public class WxPayConfiguration {
//    
//    @Autowired
//    WxProperties wxProperties;
//
//    /**
//     * 注入微信支付服务
//     * @return
//     */
//    @Bean
//    public WxPayService wxPayService() {
//        WxPayConfig payConfig = new WxPayConfig();
//        payConfig.setAppId(StringUtils.trimToNull(this.wxProperties.getAppId()));
//        payConfig.setMchId(StringUtils.trimToNull(this.wxProperties.getMchId()));
//        payConfig.setMchKey(StringUtils.trimToNull(this.wxProperties.getKey()));
//        //payConfig.setSubAppId(StringUtils.trimToNull(this.wxProperties.getSubAppId()));
//        //payConfig.setSubMchId(StringUtils.trimToNull(this.wxProperties.getSubMchId()));
//        //payConfig.setKeyPath(StringUtils.trimToNull("classpath:apiclient_cert.p12"));
//
//        // 可以指定是否使用沙箱环境
//        payConfig.setUseSandboxEnv(false);
//
//        WxPayService wxPayService = new WxPayServiceImpl();
//        wxPayService.setConfig(payConfig);
//        return wxPayService;
//    }
//}
