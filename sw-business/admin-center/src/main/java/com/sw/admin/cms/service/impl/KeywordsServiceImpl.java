package com.sw.admin.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.admin.cms.mapper.KeywordsMapper;
import com.sw.admin.cms.service.IKeyWordsService;
import com.sw.common.entity.cms.Keywords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 热闹关键词表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-12-27 14:46:03
 */
@Service("keywordsService")
public class KeywordsServiceImpl extends ServiceImpl<KeywordsMapper, Keywords> implements IKeyWordsService {

    @Autowired
    KeywordsMapper keywordsMapper;

    @Override
    public List<Map<String, String>> selectHotKeywordList() {
        return keywordsMapper.selectHotKeywordList();
    }

}
