package com.sw.common.entity.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 会员等级表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-05-18 16:03:02
 */
@Data
@TableName("snu_customer_level")
public class CustomerLevel extends BaseEntity<CustomerLevel> {

	private static final long serialVersionUID = 1L;

	// 会员等级主键
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkLevelId;

	// 等级名称
    private String levelName;

	// 等级积分
    private Integer growthPoint;

	// 状态
    private String status;

	// 是否为默认等级
    private String isDefault;

	// 免运费标准
    private BigDecimal freeFreightPoint;

	// 每次评价获取的成长值
    private Integer commentGrowthPoint;

	// 是否有免邮特权
    private String priviledgeFreeFreight;

	// 是否有签到特权
    private String priviledgeSignIn;

	// 是否有评论获奖励特权
    private String priviledgeComment;

	// 是否有专享活动特权
    private String priviledgePromotion;

	// 是否有会员价格特权
    private String priviledgeMemberPrice;

	// 是否有生日特权
    private String priviledgeBirthday;

	// 备注
    private String remark;

	@Override
    protected Serializable pkVal() {
		return pkLevelId;
	}



}
