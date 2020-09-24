package com.sw.admin.file.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.common.entity.system.File;

import java.util.Map;

/**
 * 文件相关信息
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-26 21:28:23
 */
public interface FileMapper extends BaseMapper<File> {

    /**
     * 根据fkId删除文件
     * @param params
     */
    void deleteFile(Map<String, Object> params);
}
