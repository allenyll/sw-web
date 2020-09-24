package com.sw.admin.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.system.File;

import java.util.Map;

public interface IFileService extends IService<File> {

    /**
     * 根据fkId删除文件
     * @param fkId
     */
    void deleteFile(Long fkId);

    /**
     * 处理文件
     * @param param
     */
    void dealFile(Map<String, Object> param);

    /**
     * 更新文件
     * @param params
     */
    void updateFile(Map<String, Object> params);
}
