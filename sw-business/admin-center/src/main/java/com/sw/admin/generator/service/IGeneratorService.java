package com.sw.admin.generator.service;

import com.sw.admin.generator.entity.Gen;

import java.util.List;
import java.util.Map;

/**
 * @Description:  代码自动生成服务接口
 * @Author:       allenyll
 * @Date:         2019/1/28 3:42 PM
 * @Version:      1.0
 */
public interface IGeneratorService {

    /**
     * 获取连接数据库所有表
     * @param params
     * @return
     */
    List<Map<String, Object>> getTableList(Map<String, Object> params);

    /**
     * 获取连接数据库表数量
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 生成代码
     * @param gen
     * @return
     */
    byte[] generatorCode(Gen gen);
}
