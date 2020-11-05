package com.sw.admin.cms.mapper;

import com.sw.common.entity.cms.Footprint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * 商品浏览记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-04 09:48:58
 */
public interface FootprintMapper extends BaseMapper<Footprint> {

    /**
     * 统计浏览记录总数
     * @param params
     * @return
     */
    int selectCount(Map<String, Object> params);

    /**
     * 分页查询
     * @param params
     * @return
     */
    List<Footprint> getFootprintPage(Map<String, Object> params);
}
