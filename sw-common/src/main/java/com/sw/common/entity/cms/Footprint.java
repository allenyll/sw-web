package com.sw.common.entity.cms;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import com.sw.common.entity.product.Goods;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;


/**
 * 商品浏览记录
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-04 09:48:58
 */
@ToString
@Data
@TableName("snu_footprint")
public class Footprint extends BaseEntity<Footprint>  {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	/**
	 * 商品主键
	 */
    private Long goodsId;

	/**
	 * 类型
	 */
    private String type;

	/**
	 * 状态
	 */
    private String status;

	/**
	 * 访问次数
	 */
    private Integer times;

    @TableField(exist = false)
    private Goods goods;

	@TableField(exist = false)
	private String goodsName;

	@TableField(exist = false)
	private String goodsFileUrl;

	@TableField(exist = false)
	private String customerName;

	@Override
    protected Serializable pkVal() {
		return id;
	}


}
