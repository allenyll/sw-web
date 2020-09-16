package com.sw.common.util;

import org.springframework.util.CollectionUtils;

import java.util.Collection;

public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNotEmpty(Collection<?> collection){
        return !CollectionUtils.isEmpty(collection);
    }
}
