package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * 管理员状态
 * @Author: yu.leilei
 * @Date: 下午 3:09 2018/3/9 0009
 */
public enum  UserStatus {

    OK("SW0001", "启用"),
    FREEZED("SW0002", "冻结");

    String code;
    String message;

    UserStatus(String code, String message){
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
            for(UserStatus status : UserStatus.values()){
                if(code.equals(status.getCode())){
                    return status.getMessage();
                }
            }
            return "";
        }
    }
}
