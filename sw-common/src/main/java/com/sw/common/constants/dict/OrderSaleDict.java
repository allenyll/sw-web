package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * @Description:  订单售后申请状态
 * @Author:       allenyll
 * @Date:         2020/11/19 上午9:54
 * @Version:      1.0
 */
public enum OrderSaleDict {

    /**
     * 未发起售后
     */
    START("SW0801", "未发起售后"),
    APPLY("SW0802", "申请售后"),
    CANCEL("SW0803", "取消售后"),
    DEAL("SW0804", "售后处理中"),
    REFUSE("SW0805", "拒绝售后"),
    COMPLETE("SW0806", "处理完成");


    String code;
    String message;

    OrderSaleDict(String code, String message){
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
            for(OrderSaleDict sexDict : OrderSaleDict.values()){
                if(code.equals(sexDict.getCode())){
                    return sexDict.getMessage();
                }
            }
            return "";
        }
    }
}
