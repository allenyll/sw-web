package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * 订单类型字典
 */
public enum OrderConfirmDict {

    UN_CONFIRM("SW2101", "未确认"),
    CONFIRM("SW2102", "已确认");

    String code;
    String message;

    OrderConfirmDict(String code, String message){
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
            for(OrderConfirmDict sexDict : OrderConfirmDict.values()){
                if(code.equals(sexDict.getCode())){
                    return sexDict.getMessage();
                }
            }
            return "";
        }
    }
}
