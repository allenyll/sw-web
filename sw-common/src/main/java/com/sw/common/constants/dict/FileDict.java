package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * 文件字典
 */
public enum FileDict {

    GOODS("SW1801", "商品图片"),
    CATEGORY("SW1802", "分类图片"),
    ATTR_PIC("SW1803", "商品属性图");

    String code;
    String message;

    FileDict(String code, String message){
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
            for(FileDict sexDict : FileDict.values()){
                if(code.equals(sexDict.getCode())){
                    return sexDict.getMessage();
                }
            }
            return "";
        }
    }
}
