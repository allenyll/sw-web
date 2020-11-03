package com.sw.admin.generator.entity;

import lombok.Data;

/**
 * @Description:  列属性
 * @Author:       allenyll
 * @Date:         2019/2/25 9:53 AM
 * @Version:      1.0
 */
@Data
public class Column {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列类型
     */
    private String dataType;

    /**
     * 列备注
     */
    private String comments;

    /**
     * 属性名称 首字母大写 user_name -> UserName
     */
    private String attrName;

    /**
     * 属性名称 首字母小写 user_name -> userName
     */
    private String attrNameLow;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * 其他
     */
    private String extra;

}
