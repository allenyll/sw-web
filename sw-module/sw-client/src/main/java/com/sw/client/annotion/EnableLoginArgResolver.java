package com.sw.client.annotion;

import com.sw.client.config.LoginArgResolverConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author:       allenyll
 * @Date:         2020/6/1 8:30 下午
 * @Version:      1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LoginArgResolverConfig.class)
public @interface EnableLoginArgResolver {
}
