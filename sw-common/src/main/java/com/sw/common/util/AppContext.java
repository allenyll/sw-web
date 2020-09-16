package com.sw.common.util;

/**
* @Title: AppContext
* @Package com.sw.wechat.entity
* @Description: 存储一次HTTP请求会话 wechat_openid 从而找找到对应的用户
* @author yu.leilei
* @date 2018/10/22 15:09
* @version V1.0
*/
public class AppContext implements AutoCloseable {

    private static final ThreadLocal<String> CURRENT_CONSUMER_WECHAT_OPENID = new ThreadLocal<>();

    public AppContext(String wechatOpenid) {
        CURRENT_CONSUMER_WECHAT_OPENID.set(wechatOpenid);
    }

    @Override
    public void close() {
        CURRENT_CONSUMER_WECHAT_OPENID.remove();
    }

    public static String getCurrentUserWechatOpenId() {
        return CURRENT_CONSUMER_WECHAT_OPENID.get();
    }

}