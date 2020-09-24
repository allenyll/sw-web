package com.sw.admin.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.cms.Keywords;

import java.util.List;
import java.util.Map;

public interface IKeyWordsService extends IService<Keywords> {

    /**
     * 查询热门搜索列表
     * @return
     */
    List<Map<String, String>> selectHotKeywordList();
}
