package com.sw.admin.generator.util;

import com.sw.common.util.DateUtil;
import com.sw.common.util.MapUtil;
import com.sw.admin.generator.constants.GeneratorConstants;
import com.sw.admin.generator.entity.Column;
import com.sw.admin.generator.entity.Gen;
import com.sw.admin.generator.entity.Table;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description:  代码生成工具类
 * @Author:       allenyll
 * @Date:         2019/2/25 11:09 AM
 * @Version:      1.0
 */
public class CodeUtil {

    /**
     * 生成代码
     * @param queryTable
     * @param queryColumns
     * @param zos
     * @param gen
     */
    public static void generator(Map<String, Object> queryTable, List<Map<String, Object>> queryColumns, ZipOutputStream zos, Gen gen) {

        Configuration config = getConfig();

        Table table = new Table();
        table.setTableName(MapUtil.getMapValue(queryTable, "tableName"));
        table.setComments(MapUtil.getMapValue(queryTable, "tableComment"));
        String className = getClassNameWidthPrefix(table.getTableName(), config);
        table.setClassName(className);
        table.setClassNameLow(StringUtils.uncapitalize(className));

        List<Column> columns = new ArrayList<>();
        List<Column> entitys = new ArrayList<>();
        if(!CollectionUtils.isEmpty(queryColumns)){
            for(Map<String, Object> columnMap:queryColumns){
                Column column = new Column();
                String columnName = MapUtil.getMapValue(columnMap, "columnName");

                column.setColumnName(columnName);
                column.setDataType(MapUtil.getMapValue(columnMap, "dataType"));
                column.setComments(MapUtil.getMapValue(columnMap, "columnComment"));
                column.setExtra(MapUtil.getMapValue(columnMap, "extra"));

                String attrName = replaceCase(column.getColumnName());
                column.setAttrName(attrName);
                column.setAttrNameLow(StringUtils.uncapitalize(attrName));

                String attrType = config.getString(column.getDataType(), "unkonwType");
                column.setAttrType(attrType);

                String key = MapUtil.getMapValue(columnMap, "columnKey");

                if(GeneratorConstants.PK.equalsIgnoreCase(key) && table.getPk() == null){
                    table.setPk(column);
                }

                columns.add(column);

                if(GeneratorConstants.DELETE.equals(columnName) || GeneratorConstants.ADD_TIME.equals(columnName) ||
                        GeneratorConstants.ADD_USER.equals(columnName) || GeneratorConstants.UPDATE_TIME.equals(columnName) ||
                        GeneratorConstants.UPDATE_USER.equals(columnName)){
                    continue;
                }
                entitys.add(column);
            }
        }

        table.setColumns(columns);
        table.setEntitys(entitys);

        if(table.getPk() == null){
            table.setPk(columns.get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        prop.put("input.encoding", "UTF-8");
        prop.put("output.encoding", "UTF-8");
        Velocity.init(prop);

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", table.getTableName());
        map.put("comments", table.getComments());
        map.put("pk", table.getPk());
        map.put("className", table.getClassName());
        map.put("classNameLow", table.getClassNameLow());
        map.put("pathName", table.getClassNameLow().toLowerCase());
        map.put("columns", table.getColumns());
        map.put("entitys", table.getEntitys());
        map.put("package", gen.getPackageName());
        map.put("packageEntity", config.getString("packageEntity"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtil.getCurrentDateTime());
        map.put("moduleName", gen.getModelName());
        map.put("secondModuleName", toLowerCaseFirstOne(className));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zos.putNextEntry(new ZipEntry(getFileName(template, table.getClassName(), gen.getPackageName(), gen.getModelName())));
                IOUtils.write(sw.toString(), zos, "UTF-8");
                IOUtils.closeQuietly(sw);
                zos.closeEntry();
            } catch (IOException e) {
                throw new RuntimeException("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }

    }

    public static String getClassNameWidthPrefix(String tableName, Configuration config) {
        if(tableName.contains(GeneratorConstants.SYS_PREFIX)){
            tableName = tableName.replace(GeneratorConstants.SYS_PREFIX, "");
        }else if(tableName.contains(GeneratorConstants.SNU_PREFIX)){
            tableName = tableName.replace(GeneratorConstants.SNU_PREFIX, "");
        }
        return replaceCase(tableName);
    }

    public static Configuration getConfig(){
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw  new RuntimeException("获取generator配置文件失败");
        }
    }

    public static String replaceCase(String code){
        return WordUtils.capitalizeFully(code, new char[]{'_'}).replace("_", "");
    }

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("template/index.js.vm");
        templates.add("template/index.vue.vm");
        templates.add("template/mapper.xml.vm");
        templates.add("template/service.java.vm");
        templates.add("template/IService.java.vm");
        templates.add("template/entity.java.vm");
        templates.add("template/mapper.java.vm");
        templates.add("template/controller.java.vm");
        return templates;
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        String frontPath = "ui" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("index.js.vm")) {
            //return frontPath + "api" + File.separator + moduleName + File.separator + toLowerCaseFirstOne(className) + File.separator + "index.js";
            return frontPath +  File.separator + toLowerCaseFirstOne(className) + File.separator + "index.js";
        }

        if (template.contains("index.vue.vm")) {
            //return frontPath + "views" + File.separator + moduleName + File.separator + toLowerCaseFirstOne(className) + File.separator + "index.vue";
            return frontPath +  File.separator + toLowerCaseFirstOne(className) + File.separator + "index.vue";
        }

        if (template.contains("service.java.vm")) {
            //return packagePath + "service" + File.separator + "impl" + File.separator + moduleName + File.separator + className + "ServiceImpl.java";
            return className + "ServiceImpl.java";
        }

        if (template.contains("IService.java.vm")) {
            //return packagePath + "service" + File.separator + "impl" + File.separator + moduleName + File.separator + className + "ServiceImpl.java";
            return "I" + className + "Service.java";
        }
        if (template.contains("mapper.java.vm")) {
            //return packagePath + "mapper" + File.separator + moduleName + File.separator + className + "Mapper.java";
            return className +  "Mapper.java";
        }
        if (template.contains("entity.java.vm")) {
            //return packagePath + "entity" + File.separator + moduleName + File.separator + className + ".java";
            return className + ".java";
        }
        if (template.contains("controller.java.vm")) {
            //return packagePath + "controller" + File.separator + moduleName + File.separator + className + "Controller.java";
            return className +  "Controller.java";
        }
        if (template.contains("mapper.xml.vm")) {
           // return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Mapper.xml";
            return className + "Mapper.xml";
        }
        return null;
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }


}
