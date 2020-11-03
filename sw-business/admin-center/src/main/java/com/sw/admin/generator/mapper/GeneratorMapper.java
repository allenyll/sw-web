package com.sw.admin.generator.mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author:       allenyll
 * @Date:         2019/1/28 5:23 PM
 * @Version:      1.0
 */
public interface GeneratorMapper {

    /**
     * 获取连接表总数量
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 分页查询连接表
     * @param params
     * @return
     */
    List<Map<String, Object>> getTableList(Map<String, Object> params);

    /**
     * 根据表名查询表
     * @param tableName
     * @return
     */
    Map<String, Object> queryTable(String tableName);

    /**
     * 根据表名查询表结构
     * @param tableName
     * @return
     */
    List<Map<String, Object>> queryColumn(String tableName);
}
