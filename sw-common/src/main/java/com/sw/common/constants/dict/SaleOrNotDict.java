package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * @Description:
 * @Author:       allenyll
 * @Date:         2018/12/24 9:45 AM
 * @Version:      1.0
 */
public enum SaleOrNotDict {

    /**
     * 上架
     */
    SALE("SW1401", "上架"),
    /**
     * 下架
     */
    UN_SALE("SW1402", "下架"),

    /**
     * 预售
     */
    PRE_SALE("SW1403", "预售");

    String code;
    String message;

    SaleOrNotDict(String code, String message){
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
            for(SaleOrNotDict LogType : SaleOrNotDict.values()){
                if(code.equals(LogType.getCode())){
                    return LogType.getMessage();
                }
            }
            return "";
        }
    }
}
