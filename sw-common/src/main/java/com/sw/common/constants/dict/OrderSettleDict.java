package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * 订单类型字典
 */
public enum OrderSettleDict {

    UN_SETTLE("SW1101", "未结算"),
    SETTLE("SW1102", "已结算");

    String code;
    String message;

    OrderSettleDict(String code, String message){
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
            for(OrderSettleDict sexDict : OrderSettleDict.values()){
                if(code.equals(sexDict.getCode())){
                    return sexDict.getMessage();
                }
            }
            return "";
        }
    }
}
