package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

public enum IsOrNoDict {

    YES("SW1001", "是"),
    NO("SW1002", "否");

    String code;
    String message;

    IsOrNoDict(String code, String message){
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
            for(IsOrNoDict sexDict : IsOrNoDict.values()){
                if(code.equals(sexDict.getCode())){
                    return sexDict.getMessage();
                }
            }
            return "";
        }
    }
}
