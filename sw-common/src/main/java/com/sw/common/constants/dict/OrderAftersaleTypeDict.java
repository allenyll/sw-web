package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * @Description:  订单售后类型
 * @Author:       allenyll
 * @Date:         2018/12/24 9:45 AM
 * @Version:      1.0
 */
public enum OrderAftersaleTypeDict {

    /**
     * 退款
     */
    REFUND("SW2901", "退款"),
    /**
     * 换货
     */
    CHANGE("SW2902", "换货"),
    /**
     * 退款退货
     */
    REFUND_CHANGE("SW2903", "退款退货");

    String code;
    String message;

    OrderAftersaleTypeDict(String code, String message){
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
            for(OrderAftersaleTypeDict LogType : OrderAftersaleTypeDict.values()){
                if(code.equals(LogType.getCode())){
                    return LogType.getMessage();
                }
            }
            return "";
        }
    }
}
