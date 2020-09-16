package com.sw.common.entity.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
* @Title: WxCodeResponse
* @Package com.sw.wechat.entity
* @Description: 微信登录凭证校验返回值
* @author yu.leilei
* @date 2018/10/22 9:34
* @version V1.0
*/
@Data
public class WxCodeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回，详见 UnionID 机制说明。
     */
    private String unionid;

    /**
     * 	错误码
     */
    private String errcode;

    /**
     * 错误信息
     */
    private String errMsg;

}
