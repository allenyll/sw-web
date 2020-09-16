package com.sw.common.util;

/**
 * @Description:  通用返回结果
 * @Author:       allenyll
 * @Date:         2020/6/1 7:07 下午
 * @Version:      1.0
 */
public class Result<T> extends BaseResult{

    private static final long serialVersionUID = 1L;

    private T object;

    public Result(){
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
