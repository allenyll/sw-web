package com.sw.admin.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.dict.FileDict;
import com.sw.common.entity.system.File;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.file.service.IFileService;
import com.sw.admin.file.service.impl.FileServiceImpl;
import com.sw.admin.file.util.CosFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:  腾讯云COS文件上传下载
 * @Author:       allenyll
 * @Date:         2019-05-24 17:38
 * @Version:      1.0
 */
@Slf4j
@RestController
@Api(value = "腾讯云文件上传接口")
@RequestMapping("/file")
public class FileCosController extends BaseController<FileServiceImpl, File> {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final String URL = "https://system-web-1257935390.cos.ap-chengdu.myqcloud.com";

    @Autowired
    IFileService fileService;

    @ApiOperation("上传文件")
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public DataResponse upload(@CurrentUser(isFull = true) User user, @RequestParam("file") MultipartFile file, @RequestParam String type, @RequestParam Long id) throws IOException {
        if(file == null) {
            return DataResponse.fail("上传文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        String preFix = fileName.substring(fileName.lastIndexOf("."));
        Date date = new Date();
        String time = sdf.format(date);
       // 用uuid作为文件名，防止生成的临时文件重复
       final java.io.File excelFile = java.io.File.createTempFile("imagesFile-"+time, preFix);
       /* 将MultipartFile转为File */
       file.transferTo(excelFile);

       Map<String, Object> map = CosFileUtil.uploadFile(excelFile);

       if(map == null && map.isEmpty()){
           return DataResponse.fail("上传失败");
       }

       String url = MapUtil.getString(map, "fileName", "");

       String downloadUrl = MapUtil.getString(map, "url", "");

        Map<String,Object> result = new HashMap<>();
       if(!FileDict.ATTR_PIC.getCode().equals(type)){
           // 存入数据库
           File sysFile = new File();
           Long fileId = SnowflakeIdWorker.generateId();
           result.put("fileId", fileId);
           sysFile.setId(fileId);
           sysFile.setFileType(type);
           sysFile.setFkId(id);
           sysFile.setFileUrl(URL + url);
           sysFile.setIsDelete(0);
           sysFile.setAddTime(DateUtil.getCurrentDateTime());
           sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
           fileService.save(sysFile);
       }

       result.put("url", URL + url);

       return DataResponse.success(result);
    }

    @RequestMapping(value = "/getFileList", method = RequestMethod.POST)
    public DataResponse getList(@RequestParam Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("FILE_TYPE", MapUtil.getMapValue(params, "type"));
        wrapper.eq("FK_ID", MapUtil.getMapValue(params, "id"));
        List<File> list = service.list(wrapper);
        List<Map<String, Object>> newList  = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for (File file:list){
                Map<String, Object> map = new HashMap<>();
                map.put("url", file.getFileUrl());
                map.put("id", file.getId());
                newList.add(map);
            }
        }

        result.put("list", newList);
        return DataResponse.success(result);
    }

    @ApiOperation(value = "获取附件文件")
    @RequestMapping(value = "/selectOne", method = RequestMethod.POST)
    public File selectOne(@RequestBody Map<String, Object> param) {
        QueryWrapper<File> fileEntityWrapper = new QueryWrapper<>();
        fileEntityWrapper.eq("FILE_TYPE", MapUtil.getString(param, "FILE_TYPE"));
        fileEntityWrapper.eq("IS_DELETE", 0);
        fileEntityWrapper.eq("FK_ID", MapUtil.getLong(param, "FK_ID"));
        return service.getOne(fileEntityWrapper);
    }

    @ApiOperation("保存文件")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void save(@CurrentUser(isFull = true) User user, @RequestBody File file) {
        super.add(user, file);
    }

    @ResponseBody
    @ApiOperation("获取文件列表")
    @RequestMapping(value = "/getFiles", method = RequestMethod.POST)
    public List<File> getFileList(@RequestBody File file) {
        QueryWrapper<File> fileEntityWrapper = new QueryWrapper<>();
        fileEntityWrapper.eq("FILE_TYPE", file.getFileType());
        fileEntityWrapper.eq("IS_DELETE", 0);
        fileEntityWrapper.eq("FK_ID", file.getFkId());
        return service.list(fileEntityWrapper);
    }

    @ApiOperation("更新文件")
    @RequestMapping(value = "/dealFile", method = RequestMethod.POST)
    public void dealFile(@RequestBody Map<String, Object> param) {
        fileService.dealFile(param);
    }


    @ApiOperation("根据ID删除文件")
    @RequestMapping(value = "/removeFileById", method = RequestMethod.GET)
    public void removeFileById(@CurrentUser(isFull = true) User user, @RequestParam Long fileId) {
        fileService.removeFileById(user.getId(), fileId);
    }

    @ApiOperation("删除文件")
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public void deleteFile(@RequestParam Long fkId) {
        fileService.deleteFile(fkId);
    }

    @ApiOperation("删除文件")
    @RequestMapping(value = "/deleteFileByUrl", method = RequestMethod.POST)
    public void deleteFileByUrl(@RequestBody File file) {
        fileService.deleteFileByUrl(file.getFileUrl());
    }

    @RequestMapping(value = "updateFile", method = RequestMethod.POST)
    public void updateFile(@RequestBody Map<String, Object> params) {
       fileService.updateFile(params);
    }

}
