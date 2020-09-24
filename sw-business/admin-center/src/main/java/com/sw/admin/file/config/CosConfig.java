package com.sw.admin.file.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description:  腾讯云COS文件上传
 * @Author:       allenyll
 * @Date:         2019-05-27 09:15
 * @Version:      1.0
 */
@Data
@Component
public class CosConfig {

    @Value("${cos.bucketName}")
    private String bucketName;

    @Value("${cos.region}")
    private String region;

    @Value("${cos.appId}")
    private String appId;

    @Value("${cos.SecretId}")
    private String SecretId;

    @Value("${cos.SecretKey}")
    private String SecretKey;
}
