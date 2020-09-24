package com.sw.admin.cms.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.common.entity.cms.SearchHistory;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-27 14:46:25
 */
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {

    List<Map<String, String>> selectHistoryKeywordList(Map<String, Object> params);

    int updateByCustomerId(Map<String, Object> params);
}
