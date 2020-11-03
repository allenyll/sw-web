package com.sw.admin.generator.entity;

import lombok.Data;

/**
 * @Description:  代码配置属性
 * @Author:       allenyll
 * @Date:         2019/2/25 4:57 PM
 * @Version:      1.0
 */
@Data
public class Gen {

    private String tableName;

    private String tablePrefix;

    private String className;

    private String modelName;

    private String packageName;

}
