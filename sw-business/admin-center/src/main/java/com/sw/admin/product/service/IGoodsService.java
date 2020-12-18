package com.sw.admin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.dto.GoodsQueryDto;
import com.sw.common.dto.GoodsResult;
import com.sw.common.entity.product.Goods;
import com.sw.common.util.Result;

public interface IGoodsService extends IService<Goods> {

    /**
     * 设置商品相关属性
     * @param goods
     */
    void setFile(Goods goods);

    /**
     *
     * @param goodsQueryDto
     * @return
     */
    Result<GoodsResult> getGoodsListByCondition(GoodsQueryDto goodsQueryDto);
}
