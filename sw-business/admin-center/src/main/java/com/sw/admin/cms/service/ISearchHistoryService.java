package com.sw.admin.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.cms.SearchHistory;

import java.util.List;
import java.util.Map;

public interface ISearchHistoryService extends IService<SearchHistory> {

    List<Map<String, String>> selectHistoryKeywordList(Map<String, Object> params);

    int updateByCustomerId(Map<String, Object> params);

}
