package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * 订单售后字典
 */
public enum PromotionDict {

    NO("SW2001", "无优惠"),
    DISCOUNT("SW2002", "特惠促销"),
    MEMBER("SW2003", "会员优惠"),
    LADDER("SW2004", "阶梯优惠"),
    FULL("SW2005", "满减优惠");

    String code;
    String message;

    PromotionDict(String code, String message){
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
            for(PromotionDict sexDict : PromotionDict.values()){
                if(code.equals(sexDict.getCode())){
                    return sexDict.getMessage();
                }
            }
            return "";
        }
    }
}
