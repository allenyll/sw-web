package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * @Description:  日志类型
 * @Author:       allenyll
 * @Date:         2018/12/24 9:45 AM
 * @Version:      1.0
 */
public enum PayTypeDict {

    BLANCE("SW0901", "余额"),
    WECHAT("SW0902", "微信"),
    ALIPAY("SW0903", "支付宝"),
    UNION("SW0904", "银联");

    String code;
    String message;

    PayTypeDict(String code, String message){
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据编码获取编码信息
     * @param code
     * @return
     */
    public static String codeToMessage(String code){
        if(StringUtil.isEmpty(code)){
            return "";
        }else{
            for(PayTypeDict LogType : PayTypeDict.values()){
                if(code.equals(LogType.getCode())){
                    return LogType.getMessage();
                }
            }
            return "";
        }
    }
}
