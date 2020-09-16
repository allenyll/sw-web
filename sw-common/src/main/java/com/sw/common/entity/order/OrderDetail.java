package com.sw.common.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 订单明细表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-26 21:15:58
 */
@Data
@TableName("snu_order_detail")
public class OrderDetail extends BaseEntity<OrderDetail> {

	private static final long serialVersionUID = 1L;

	// 订单明细主键
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkOrderDetailId;

	// 订单Id
    private String fkOrderId;

	// 订单编码
    private String orderNo;

	// 商品id
    private String fkGoodsId;

	// 分类ID
	private String fkCategoryId;

	// 商品名称
    private String goodsName;

	// 商品价格
    private BigDecimal goodsPrice;

	// 商品数量
    private Integer quantity;

	// 商品总价
    private BigDecimal totalAmount;

	// 商品品牌
    private String brandName;

	// 商品图片
    private String pic;

	// 库存计算方式(10下单减库存 20付款减库存)
    private String deductStockType;

	// SKU ID
    private String fkSkuId;

	// SKU 编码
    private String skuCode;

	// 商品属性
    private String specValue;

    private String attributes;

	// 商品重量
    private BigDecimal goodsWeight;

	// 商品划线价
    private BigDecimal linePrice;

	// 优惠名称
    private String promotionName;

	// 商品促销分解金额
    private BigDecimal promotionAmount;

	// 优惠券优惠分解金额
    private BigDecimal couponAmount;

	// 积分优惠分解金额
    private BigDecimal integrationAmount;

	// 积分优惠分解金额
    private BigDecimal realAmount;

	// 商品详情
    private String content;

	// 备注
    private String remark;

	@Override
    protected Serializable pkVal() {
		return pkOrderDetailId;
	}



}
