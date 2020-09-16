package com.sw.common.util;

import com.sw.common.constants.BaseConstants;

import java.io.Serializable;

public class BaseResult implements Serializable {

    private static final long serialVersionUID = 1L;

    protected boolean success;

    protected  String code;

    protected String message;

    public BaseResult() {
        this.success = true;
        this.code = BaseConstants.SUCCESS;
        this.message = "操作成功！";
    }

    public void fail(String message) {
        this.success = false;
        this.code = BaseConstants.FAIL;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
}
