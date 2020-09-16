package com.sw.common.entity.wechat;

import com.sw.common.constants.Gender;
import lombok.Data;

/**
* @Title: WxUserInfo
* @Package com.sw.wechat.entity
* @Description: 微信用户
* @author yu.leilei
* @date 2018/10/19 17:45
* @version V1.0
*/
@Data
public class WxUserInfo {

    private Long id;
    private String username;
    private Long phone;
    private Gender gender;
    private String vcode;
    private String password;
    private String promotionCode;
    private String InvitationCode;
    private String clientAssertion;
    private String code;

    public WxUserInfo() {
    }
}
