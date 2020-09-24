package com.sw.admin.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.admin.cms.mapper.SearchHistoryMapper;
import com.sw.admin.cms.service.ISearchHistoryService;
import com.sw.common.entity.cms.SearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-27 14:46:25
 */
@Service("searchHistoryService")
public class SearchHistoryServiceImpl extends ServiceImpl<SearchHistoryMapper, SearchHistory> implements ISearchHistoryService {

    @Autowired
    SearchHistoryMapper searchHistoryMapper;

    @Override
    public List<Map<String, String>> selectHistoryKeywordList(Map<String, Object> params) {
        return searchHistoryMapper.selectHistoryKeywordList(params);
    }

    @Override
    public int updateByCustomerId(Map<String, Object> params) {
        return searchHistoryMapper.updateByCustomerId(params);
    }
}
