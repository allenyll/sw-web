package com.sw.admin.generator.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description:  表属性
 * @Author:       allenyll
 * @Date:         2019/2/25 9:53 AM
 * @Version:      1.0
 */
@Data
public class Table {

    /**
     * 表的名称
     */
    private String tableName;

    /**
     * 表的备注
     */
    private String comments;

    /**
     * 表的主键
     */
    private Column pk;

    /**
     * 表的列名(不包含主键)
     */
    private List<Column> columns;

    /**
     * 表的列名(不包含主键)
     */
    private List<Column> entitys;

    /**
     * 类名(第一个字母大写)，如：sys_user => SysUser
     */
    private String className;

    /**
     * 类名(第一个字母小写)，如：sys_user => sysUser
     */
    private String classNameLow ;
}
