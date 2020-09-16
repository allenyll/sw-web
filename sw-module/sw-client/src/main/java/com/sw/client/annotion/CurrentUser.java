package com.sw.client.annotion;

import java.lang.annotation.*;

/**
 * @Description:  绑定当前登录用户
 * @Author:       allenyll
 * @Date:         2020/6/1 8:13 下午
 * @Version:      1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 是否查询User对象所有信息，true则通过rpc接口查询
     */
    boolean isFull() default false;
}
