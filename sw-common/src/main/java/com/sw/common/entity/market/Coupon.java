package com.sw.common.entity.market;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * snu_coupon
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-28 09:13:54
 */
@Data
@TableName("snu_coupon")
public class Coupon extends BaseEntity<Coupon> {

	private static final long serialVersionUID = 1L;

	// 优惠券主键
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkCouponId;

	// 名称
    private String name;

	// 金额
    private String code;

	// 金额
    private BigDecimal amount;

	// 优惠券类型
    private String type;

	// 数量
    private Integer count;

	// 每人限领张数
    private Integer perLimit;

	// 使用门槛；0表示无门槛
    private BigDecimal minPoint;

	// 开始时间
    private String startTime;

	// 结束时间
    private String endTime;

	// 使用类型：全场通用；指定分类；指定商品
    private String useType;

	// 备注
    private String note;

	// 发行数量
    private Integer publishCount;

	// 已使用数量
    private Integer useCount;

	// 领取数量
    private Integer receiveCount;

	// 可以领取的日期
    private String enableTime;

    @TableField(exist = false)
    private List<Map<String, Object>> couponGoodsList;

	@Override
    protected Serializable pkVal() {
		return pkCouponId;
	}



}
