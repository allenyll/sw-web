package com.sw.common.entity.product;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @Description:  商品参数
 * @Author:       allenyll
 * @Date:         2019-05-28 16:05
 * @Version:      1.0
 */
@Data
@ToString
public class GoodsParam extends Goods {

    private Brand brand;

    private String parentCategoryId;

    private String parentSpecCategoryId;

    private List<GoodsFullReduce> goodsFullReduceList;

    private List<GoodsLadder> goodsLadderList;

    private List<Sku> skuStockList;

    private List<Map<String, Object>> specsList;

    private List<Map<String, Object>> skuStockMapList;

    private List<Map<String, String>> selectSkuPics;
}
