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
 * 商品阶梯价格关联
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-28 17:24:48
 */
@Data
@TableName("snu_goods_ladder")
public class GoodsLadder extends Model<GoodsLadder> {

	private static final long serialVersionUID = 1L;

	// 主键ID
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkLadderId;

	// 商品主键
    private String fkGoodsId;

	// 数量
    private BigDecimal count;

	// 折扣
    private BigDecimal discount;

	// 价格
    private BigDecimal price;

	@TableField(exist = false)
	private boolean isDefault;

	@Override
    protected Serializable pkVal() {
		return pkLadderId;
	}



}
