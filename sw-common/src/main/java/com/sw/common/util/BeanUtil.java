package com.sw.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description:  实体工具类
 * @Author:       allenyll
 * @Date:         2019-06-01 23:01
 * @Version:      1.0
 */
public class BeanUtil {

    public static <T>void fatherToChild(T father,T child) throws Exception {
        if (child.getClass().getSuperclass()!=father.getClass()){
            throw new Exception("child 不是 father 的子类");
        }
        Class<?> fatherClass = father.getClass();
        Field[] declaredFields = fatherClass.getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            if("serialVersionUID".equals(field.getName())){
                continue;
            }
            Method method = fatherClass.getDeclaredMethod("get" + upperHeadChar(field.getName()));
            Object obj = method.invoke(father);
            field.setAccessible(true);
            field.set(child,obj);
        }
    }

    /**
     * 首字母大写，in:deleteDate，out:DeleteDate
     */
    public static String upperHeadChar(String in) {
        String head = in.substring(0, 1);
        String out = head.toUpperCase() + in.substring(1);
        return out;
    }

}
