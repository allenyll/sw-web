package com.sw.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import com.sw.common.entity.system.File;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 商品基本信息表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-03-21 10:51:24
 */
@Data
@TableName("snu_goods")
public class Goods extends BaseEntity<Goods> {

	private static final long serialVersionUID = 1L;

	// 商品主键
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkGoodsId;

	// 品牌主键
    private String fkBrandId;

    private String fkSpecCategoryId;

    private String fkCategoryId;

	// 单位主键
    private String fkUnitId;

	// 商品名称
    private String goodsName;

	// 商品编码
    private String goodsCode;

	// 商品条形码
    private String goodsBarCode;

	// 商品标签
    private String goodsLabel;

	// 商品价格
    private BigDecimal price;

	// 市场价
    private BigDecimal marketPrice;

	// 成本价
    private BigDecimal costPrice;

	// 库存量
    private Integer stock;

	// 告警库存
    private Integer warningStock;

	// 商品积分
    private Integer goodsIntegral;

	// 商品图片
    private String goodsUrl;

    // 商品简介
    private String goodsBrief;

	// 商品详情
    private String goodsDesc;

	// 季节性
    private String season;

	// 单位
    private String unit;

	// 商品排序
    private Integer goodsSeq;

	// 是否启用 SW1301 未启用 SW1302 启用
    private String isUsed;

	// 商品状态 SW1401 上架 SW1402 下架 SW1403 预售
    private String status;

    // 是否有规格
    private String isSpec;

    // 是否精品
    private String isBest;

    // 是否热卖
    private String isHot;

    // 是否新品
    private String isNew;

    // 是否推荐
    private String isRecom;

    // 总销量
    private Integer saleNum;

    // 上架时间
    private String saleTime;

    // 访问量
    private Integer visitNum;

    // 评价数
    private Integer appraiseNum;

    // 购买赠送多少会员成长值
    private Integer giftGrowth;

    //
    private Integer pointLimit;

    // 重量
    private BigDecimal weight;

    // 以逗号分割的产品服务：1->无忧退货；2->快速退款；3->免费包邮
    private String serviceIds;

    // 关键字
    private String keywords;

    private BigDecimal promotionPrice;

    // 促销开始时间
    private String promotionStartTime;

    // 促销结束时间
    private String promotionEndTime;

    // 促销限购数量
    private String promotionPerLimit;

    // 促销类型
    private String promotionType;

    // 促销信息
    private String goodsTips;

	// 备注
    private String remark;

    @TableField(exist = false)
    private List<File> fileList;

    @TableField(exist = false)
    private String fileUrl;

	@Override
    protected Serializable pkVal() {
		return pkGoodsId;
	}



}
