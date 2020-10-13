package com.sw.admin.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.system.File;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.DateUtil;
import com.sw.admin.file.mapper.FileMapper;
import com.sw.admin.file.service.IFileService;
import com.sw.common.util.MapUtil;
import com.sw.common.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件相关信息
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-26 21:28:23
 */
@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    @Autowired
    FileMapper fileMapper;

    @Override
    public void deleteFile(Long fkId) {
        Map<String, Object> params = new HashMap<>();
        params.put("TIME", DateUtil.getCurrentDateTime());
        params.put("FK_ID", fkId);
        fileMapper.deleteFile(params);
    }

    @Override
    public void dealFile(Map<String, Object> param) {
        Long userId = MapUtil.getLong(param, "USER_ID");
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("FILE_TYPE", MapUtil.getString(param, "FILE_TYPE"));
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("FK_ID", MapUtil.getString(param, "FK_ID"));
        File sysFile = fileMapper.selectOne(wrapper);
        // 存入数据库
        if(sysFile != null){
            sysFile.setFileUrl(MapUtil.getString(param, "URL"));
            sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
            sysFile.setUpdateUser(userId);
            fileMapper.update(sysFile, wrapper);
        }else{
            sysFile = new File();
            sysFile.setId(SnowflakeIdWorker.generateId());
            sysFile.setFileType(MapUtil.getString(param, "FILE_TYPE"));
            sysFile.setFkId(MapUtil.getLong(param, "FK_ID"));
            sysFile.setFileUrl(MapUtil.getString(param, "URL"));
            sysFile.setAddTime(DateUtil.getCurrentDateTime());
            sysFile.setIsDelete(0);
            sysFile.setAddUser(userId);
            sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
            sysFile.setUpdateUser(userId);
            fileMapper.insert(sysFile);
        }
    }

    @Override
    public void updateFile(Map<String, Object> params) {
        String fileType = MapUtil.getString(params, "FILE_TYPE");
        List<Map<String, String>> fileList = (List<Map<String, String>>) params.get("fileList");
        Long fkId = MapUtil.getLong(params, "FK_ID");
        // 先删除所有关联的图片，在增加
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("FILE_TYPE", fileType);
        wrapper.eq("FK_ID", fkId);
        fileMapper.delete(wrapper);
        if (CollectionUtil.isNotEmpty(fileList)) {
            for (Map<String, String> file:fileList) {
                // 存入数据库
                File sysFile = new File();
                sysFile.setId(SnowflakeIdWorker.generateId());
                sysFile.setFileType(fileType);
                sysFile.setFkId(fkId);
                sysFile.setFileUrl(MapUtil.getString(file, "url"));
                sysFile.setIsDelete(0);
                sysFile.setAddTime(DateUtil.getCurrentDateTime());
                sysFile.setUpdateTime(DateUtil.getCurrentDateTime());
                fileMapper.insert(sysFile);
            }
        }
    }

    @Override
    public void removeFileById(Long userId, Long fileId) {
        fileMapper.deleteById(fileId);
    }
}
