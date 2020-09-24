package com.sw.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品满减
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-28 17:24:36
 */
@Data
@TableName("snu_goods_full_reduce")
public class GoodsFullReduce extends Model<GoodsFullReduce> {

	private static final long serialVersionUID = 1L;

	// 满减主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long reduceId;

	// 商品主键
    private Long goodsId;

	// 满足价格
    private BigDecimal fullPrice;

	// 减去价格
    private BigDecimal reducePrice;

    @TableField(exist = false)
    private boolean isDefault;

}
