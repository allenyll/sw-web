package com.sw.admin.generator.controller;

import com.netflix.ribbon.proxy.annotation.Http;
import com.sw.common.util.DataResponse;
import com.sw.admin.generator.entity.Gen;
import com.sw.admin.generator.service.IGeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @Description:  代码自动生成控制器
 * @Author:       allenyll
 * @Date:         2019/1/28 3:43 PM
 * @Version:      1.0
 */
@Controller
@RequestMapping("/generator")
@Api(value = "代码自动生成接口", tags = "代码生成工具")
public class GeneratorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorController.class);

    @Autowired
    IGeneratorService generatorService;

    /**
     * 当前页数
     */
    protected int page = 1;

    /**
     * 每页数量
     */
    protected int limit = 10;

    @ApiOperation(value = "获取数据库所有表")
    @GetMapping("/getTableListByPage")
    @ResponseBody
    public DataResponse getTableList(@RequestParam Map<String, Object> params){
        LOGGER.info("============= {开始调用方法：getUserList(} =============");
        Map<String, Object> result = new HashMap<>();
        LOGGER.info("传入参数=============" + params);
        page = Integer.parseInt(params.get("page").toString());
        limit = Integer.parseInt(params.get("limit").toString());

        int start = (page - 1) * limit;
        params.put("start", start);
        params.put("limit", limit);

        int total = generatorService.selectCount(params);

        List<Map<String, Object>> list = generatorService.getTableList(params);

        result.put("total", total);
        result.put("list", list);
        return DataResponse.success(result);
    }

    /**
     * 执行代码生成
     * @param gen
     * @param response
     * @throws IOException
     */
    @RequestMapping("/code")
    public void generator(@RequestBody Gen gen, HttpServletResponse response) throws IOException {

        byte[] data = generatorService.generatorCode(gen);

        response.reset();
        // CORS "pre-flight" request
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//        response.addHeader("Access-Control-Allow-Headers", "*");
//        response.addHeader("Access-Control-Max-Age", "1800");//30 min
        response.setHeader("Content-Disposition", "attachment; filename=\"代码.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream; charset=UTF-8");


        IOUtils.write(data, response.getOutputStream());

    }

    public void testZip(HttpServletResponse response) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(os);

    }

}
