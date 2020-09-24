package com.sw.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Maps;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;


/**
 * 商品库存单位
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-28 16:22:46
 */
@Data
@TableName("snu_sku")
public class Sku extends BaseEntity<Sku> {

	private static final long serialVersionUID = 1L;

	// 主键id
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 商品主键
    private Long goodsId;

	// 名称
    private String skuName;

	// 编码
    private String skuCode;

	// 条形码
    private String skuBarCode;

	// 数量
    private Integer skuNum;

	// 规格值 [id,value];[id,value]
    private String specValue;

	// 库存
    private Integer skuStock;

	// 告警库存
    private Integer warnStock;

	// 价格
    private BigDecimal skuPrice;

	// 状态
    private String skuStatus;

	// 规格图
    private String picUrl;

	public Map<String, Object> toMap () {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", id);
		map.put("goodsId", goodsId);
		map.put("skuName", skuName);
		map.put("skuCode", skuCode);
		map.put("skuBarCode", skuBarCode);
		map.put("skuNum", skuNum);
		map.put("specValue", specValue);
		map.put("skuStock", skuStock);
		map.put("warnStock", warnStock);
		map.put("skuPrice", skuPrice);
		map.put("skuStatus", skuStatus);
		map.put("picUrl", picUrl);
		return map;
	}

}
