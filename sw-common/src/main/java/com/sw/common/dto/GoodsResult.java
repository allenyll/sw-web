package com.sw.common.dto;

import com.sw.common.entity.product.Goods;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品接口返回值统一
 * @ClassName: com.sw.common.dto.GoodsResult.java
 * @Description:
 * @author: 20012055 yuleilei
 * @date:  2020/12/18 14:28
 * @version V1.0
 */
@Data
public class GoodsResult {

    /**
     * 分页--当前页
     */
    private Integer currentPage;

    /**
     * 分页--总页码
     */
    private Integer totalPage;

    /**
     * 商品集合
     */
    private List<Goods> goodsList;

    /**
     * 总商品数量
     */
    private Integer totalSkuNum;

    /**
     * 总库存数量
     */
    private Integer totalStock;

    /**
     * 总库存成本
     */
    private BigDecimal totalCost;

    /**
     * 总库存成本，以万展示，保留2为小数
     */
    private BigDecimal cost;

    /**
     * 总告警库存数量
     */
    private Integer totalWarnStock;
}
