package com.sw.common.constants.dict;

import com.sw.common.util.StringUtil;

/**
 * 订单状态字典
 */
public enum CouponDict {

    ALL("SW2201", "全场赠送"),
    MEMBER("SW2202", "会员赠送"),
    SHOP("SW2203", "购物赠送"),
    REGISTER("SW2204", "注册赠送"),
    EXCHANGE("SW2205", "兑换赠送"),

    ALL_USE("SW2301", "全场通用"),
    CATEGORY("SW2302", "指定分类"),
    GOODS("SW2303", "指定商品"),


    UN_USE("SW2401", "未使用"),
    IS_USE("SW2402", "已使用"),
    EXPIRE("SW2403", "已过期"),

    ACTIVE_ACQUIRE("SW2501", "主动获取"),
    BACKSTAGE_GIFT("SW2502", "后台赠送");


    String code;
    String message;

    CouponDict(String code, String message){
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
            for(CouponDict sexDict : CouponDict.values()){
                if(code.equals(sexDict.getCode())){
                    return sexDict.getMessage();
                }
            }
            return "";
        }
    }
}
