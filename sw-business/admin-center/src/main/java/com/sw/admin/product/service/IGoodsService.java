package com.sw.admin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.entity.product.Goods;

public interface IGoodsService extends IService<Goods> {

    /**
     * 设置商品相关属性
     * @param goods
     */
    void setFile(Goods goods);
}
