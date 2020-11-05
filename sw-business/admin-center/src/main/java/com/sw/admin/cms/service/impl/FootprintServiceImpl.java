package com.sw.admin.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.cms.service.IFootprintService;
import com.sw.admin.product.service.IGoodsService;
import com.sw.common.entity.product.Goods;
import com.sw.common.util.CollectionUtil;
import com.sw.common.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.cms.Footprint;
import com.sw.admin.cms.mapper.FootprintMapper;

import java.util.List;
import java.util.Map;

/**
 * 商品浏览记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-04 09:48:58
 */
@Service("footprintService")
public class FootprintServiceImpl extends ServiceImpl<FootprintMapper,Footprint> implements IFootprintService {

    @Autowired
    private FootprintMapper footprintMapper;

    @Autowired
    private IGoodsService goodsService;

    @Override
    public Result<List<Footprint>>  getFootprint(Long customerId) {
        Result<List<Footprint>> result = new Result<>();
        QueryWrapper<Footprint> wrapper = new QueryWrapper<>();
        wrapper.eq("add_user", customerId);
        wrapper.eq("is_delete", 0);
        wrapper.eq("type", "商品");
        List<Footprint> isFootPrint = footprintMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(isFootPrint)) {
            for (Footprint footprint:isFootPrint) {
                Long goodsId = footprint.getGoodsId();
                Goods goods = goodsService.getById(goodsId);
                if (goods == null) {
                    continue;
                }
                goodsService.setFile(goods);
                footprint.setGoods(goods);
            }
        }
        result.setObject(isFootPrint);
        return result;
    }

    @Override
    public int selectCount(Map<String, Object> params) {
        Integer num = footprintMapper.selectCount(params);
        return num == null ? 0 : num.intValue();
    }

    @Override
    public List<Footprint> getFootprintPage(Map<String, Object> params) {
        int page = Integer.parseInt(params.get("page").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        int start = (page - 1) * limit;
        params.put("start", start);
        params.put("limit", limit);
        List<Footprint> list = footprintMapper.getFootprintPage(params);
        return list;
    }
}
