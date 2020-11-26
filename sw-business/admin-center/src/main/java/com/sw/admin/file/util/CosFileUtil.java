package com.sw.admin.file.util;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.sw.admin.file.config.CosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:  cos文件上传工具
 * @Author:       allenyll
 * @Date:         2019-05-27 11:05
 * @Version:      1.0
 */
@Slf4j
@Component
public class CosFileUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");

    private static CosConfig cosConfig;

    @Autowired
    private CosConfig cosConfigBefore;

    private static COSCredentials cred = null;

    private static COSClient cosClient = null;

    private static ClientConfig clientConfig = null;

    private static TransferManager transferManager = null;

    // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
    private static String bucketName;

    @PostConstruct
    public void beforeInit() {
        cosConfig = this.cosConfigBefore;
    }

    @Bean
    private static TransferManager transferManager() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        cred = new BasicCOSCredentials(cosConfig.getSecretId(), cosConfig.getSecretKey());
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        clientConfig = new ClientConfig(new Region(cosConfig.getRegion()));
        bucketName = cosConfig.getBucketName();
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
        //指定要上传到COS上的路径
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        transferManager = new TransferManager(cosClient, executorService);
        return transferManager;
    }

    /**
     * 上传文件
     * @param localFile
     * @param threadLocal
     */
    public static void uploadFile(File localFile, ThreadLocal threadLocal) {
        new Thread(() -> {
            try {
                String key = localFile.getName();
                String substring = key.substring(key.lastIndexOf("."));
                Random random = new Random();
                // 指定要上传到 COS 上的路径
                key = "/images/" + getTime() + "/" + random.nextInt(10000) + System.currentTimeMillis() + substring;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                // 本地文件上传
                Upload upload = transferManager.upload(putObjectRequest);
                // 异步（如果想等待传输结束，则调用 waitForUploadResult）
                //UploadResult uploadResult = upload.waitForUploadResult();
                //同步的等待上传结束waitForCompletion
                upload.waitForCompletion();
                log.debug("上传结束时间: {}", sdf.format(new Date()), " + 上传成功");
                //获取上传成功之后文件的下载地址
                URL url = cosClient.generatePresignedUrl(bucketName + "-" + cosConfig.getAppId(), key, new Date(new Date().getTime() + 5 * 60 * 10000));
            } catch (InterruptedException e) {
                log.error("上传失败");
                e.printStackTrace();
            } finally {
                transferManager.shutdownNow();
                cosClient.shutdown();
            }
        }).start();
    }

    /**
     * 上传文件
     * param localFile
     * @return
     */
    public static Map<String, Object> uploadFile(File localFile) {
        Map<String, Object> result = new HashMap<>();

        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
        Random random = new Random();
        String key = localFile.getName();
        String substring = key.substring(key.lastIndexOf("."));
        // 指定要上传到 COS 上的路径
        key = "/images/" + getTime() + "/" + random.nextInt(10000) + System.currentTimeMillis() + substring;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        log.debug("上传结束时间: {}", sdf.format(new Date()), " + 上传成功");
        URL url = cosClient.generatePresignedUrl(bucketName + "-" + cosConfig.getAppId(), key, new Date(new Date().getTime() + 5 * 60 * 10000));
        result.put("fileName", key);
        result.put("url", url);
        return result;
    }

    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        String time = year + "" + month + "" + day;
        return time;
    }

    /**
     *
     * @Title: downFile
     * @Description: 下载文件
     * @return
     */
    public static void downFile() {
        // 生成cos客户端
        cosClient = new COSClient(cred, clientConfig);
        //要下载的文件路径和名称
        String key = "down/demo1.jpg";
        // 指定文件的存储路径
        File downFile = new File("src/test/resources/mydown.txt");
        // 指定要下载的文件所在的 bucket 和对象键
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        ObjectMetadata downObjectMeta = cosClient.getObject(getObjectRequest, downFile);
    }


    /**
     * 删除文件
     *
     * @param key
     */
    public static void deletefile(String key) throws CosClientException, CosServiceException {
        // 生成cos客户端
        cosClient = new COSClient(cred, clientConfig);
        // 指定要删除的 bucket 和路径
        cosClient.deleteObject(bucketName, key);
        // 关闭客户端(关闭后台线程)
        cosClient.shutdown();
    }

}
