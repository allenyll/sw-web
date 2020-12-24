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
     * 获取商品列表
     * @param goodsQueryDto
     * @return
     */
    Result<GoodsResult> getGoodsListByCondition(GoodsQueryDto goodsQueryDto);

    /**
     * 获取商品库存信息
     * @param goodsQueryDto
     * @return
     */
    Result<GoodsResult> getStock(GoodsQueryDto goodsQueryDto);
}
