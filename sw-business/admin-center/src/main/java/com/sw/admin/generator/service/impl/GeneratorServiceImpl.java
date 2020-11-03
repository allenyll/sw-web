package com.sw.admin.generator.service.impl;

import com.sw.common.util.StringUtil;
import com.sw.admin.generator.entity.Gen;
import com.sw.admin.generator.mapper.GeneratorMapper;
import com.sw.admin.generator.service.IGeneratorService;
import com.sw.admin.generator.util.CodeUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @Description:  代码自动生成服务
 * @Author:       allenyllgenerator.properties
 * @Date:         2019/1/28 3:41 PM
 * @Version:      1.0
 */
@Service("generatorService")
public class GeneratorServiceImpl implements IGeneratorService {

    @Autowired
    GeneratorMapper generatorMapper;

    @Override
    public List<Map<String, Object>> getTableList(Map<String, Object> params) {

        return generatorMapper.getTableList(params);
    }

    @Override
    public int selectCount(Map<String, Object> params) {
        return generatorMapper.selectCount(params);
    }

    @Override
    public byte[] generatorCode(Gen gen) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);
        String tableName = gen.getTableName();
        if(StringUtil.isNotEmpty(tableName)){
            Map<String, Object> queryTable = generatorMapper.queryTable(tableName);

            List<Map<String, Object>> queryColumn = generatorMapper.queryColumn(tableName);

            CodeUtil.generator(queryTable, queryColumn, zos, gen);
        }
        IOUtils.closeQuietly(zos);

        return os.toByteArray();
    }

}
