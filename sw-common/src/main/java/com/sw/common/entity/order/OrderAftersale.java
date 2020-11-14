package com.sw.common.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 售后申请
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-10 17:36:28
 */
@ToString
@Data
@TableName("snu_order_aftersale")
public class OrderAftersale extends BaseEntity<OrderAftersale>  {

	private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
    * 订单ID
    */
    private Long orderId;

    /**
    * 订单明细ID
    */
    private Long orderDetailId;

    /**
    * 商品ID
    */
    private Long goodsId;

    /**
    * 用户ID
    */
    private Long customerId;

    /**
    * 退货
            换货
            退款退货
    */
    private String aftersaleType;

    /**
    * SW0801 未发起售后
    * SW0802 申请售后
    SW0803 取消售后
    SW0804 售后处理中
    SW0805 拒绝申请
    SW0806 处理完成
    */
    private String aftersaleStatus;

    /**
    * 申请原因
    */
    private String aftersaleReason;

    /**
    * 申请佐证，支持多个，逗号分开
    */
    private String applyFile;

    /**
    * 退还积分
    */
    private Integer refundPoint;

    /**
    * 使用的积分可抵扣金额
    */
    private BigDecimal usePointAmount;

    /**
    * 获得的积分可抵扣金额
    */
    private BigDecimal getPointAmount;

    /**
    * 申请退款金额
    */
    private BigDecimal refundAmount;

    /**
    * 原来返回
            退还到余额
    */
    private String refundType;

    /**
    * 退货数量
    */
    private Integer refundQuality;

    /**
    * 退款说明
    */
    private String refundRemark;

    /**
    * 上门取件
            送到门店
            快递
    */
    private String returnType;

    /**
    * 拒绝原因
    */
    private String refuseReason;

    /**
    * 快递名称
    */
    private String deliveryCompany;

    /**
    * 快递单号
    */
    private String deliveryNo;

    /**
    * 收货人
    */
    private String receiverName;

    /**
    * 收货人电话
    */
    private String receiverPhone;

    /**
    * 收货人邮编
    */
    private String receiverPostCode;

    /**
    * 收货人省份
    */
    private String receiverProvince;

    /**
    * 收货人城市
    */
    private String receiverCity;

    /**
    * 收货人区域
    */
    private String receiverRegion;

    /**
    * 收货人详细地址
    */
    private String receiverDetailAddress;

    /**
    * 退货时间
    */
    private String deliveryTime;

    /**
    * 申请时间
    */
    private String applyTime;

    /**
    * 处理时间
    */
    private String dealTime;

    /**
    * 审核时间
    */
    private String auditTime;

    /**
    * 取消时间
    */
    private String cancelTime;

    /**
    * 处理人
    */
    private Long dealUser;

    /**
    * 处理人姓名
    */
    private String dealUserName;

    /**
    * 处理备注
    */
    private String dealNote;

	@Override
    protected Serializable pkVal() {
		return id;
	}

}
