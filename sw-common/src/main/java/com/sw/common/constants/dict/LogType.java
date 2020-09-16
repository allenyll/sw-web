package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * @Description:  日志类型
 * @Author:       allenyll
 * @Date:         2018/12/24 9:45 AM
 * @Version:      1.0
 */
public enum LogType {

    SYS_LOG("SW0301", "系统日志"),
    OPR_LOG("SW0302", "操作日志");

    String code;
    String message;

    LogType(String code, String message){
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
            for(com.sw.common.constants.dict.LogType LogType : LogType.values()){
                if(code.equals(LogType.getCode())){
                    return LogType.getMessage();
                }
            }
            return "";
        }
    }
}
