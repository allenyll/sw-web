package com.sw.common.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerAddress;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 订单基础信息表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-26 21:11:07
 */
@Data
@TableName("snu_order")
public class Order extends BaseEntity<Order> {

	private static final long serialVersionUID = 1L;

	// 订单主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 订单编码
    private String orderNo;

	// 用户ID
    private Long customerId;

	// 订单类型   SW0601 线上  SW0602 线下
    private String orderType;

	// 订单状态 SW0701 未付款 SW0702 已付款  SW0703 已收货 SW0704 已评价 SW0705 已完成 SW0706 已取消
    private String orderStatus;

	// 售后状态 SW0801 未发起售后 SW0802 申请售后 SW0803 取消售后 SW0804 售后处理中 SW0805 处理完成
    private String afterStatus;

	// 商品数量
    private Integer goodsCount;

	// 订单总价
    private BigDecimal orderAmount;

	// 支付金额
    private BigDecimal payAmount;

	// 最终金额
    private BigDecimal totalAmount;

	// 运费金额
    private BigDecimal logisticsFee;

	// 收货地址
    private Long addressId;

	// 支付渠道 SW0901 余额 SW0902 微信 SW0903 支付宝 SW0904 银联
    private String payChannel;

	// 订单支付单号
    private String tradeNo;

	// 第三方支付流水
    private String escrowTradeNo;

	// 支付时间
    private String payTime;

	// 订单创建时间
    private String orderTime;

    // 快递公司
    private String deliveryCompany;

    // 快递单号
    private String deliveryNo;

	// 发货时间
    private String deliveryTime;

	// 确认状态
    private String confirmStatus;

	// 收货时间
    private String receiveTime;

	// 评论时间
    private String commentTime;

	// 订单结算状态 SW1101 未结算 SW1102 已结算
    private String settlementStatus;

	// 订单结算时间
    private String settlementTime;

	// 优惠券
    private Long couponId;

	// 促销优化金额（促销价、满减、阶梯价）
    private BigDecimal promotionAmount;

	// 积分抵扣金额
    private BigDecimal integrationAmount;

	// 优惠券抵扣金额
    private BigDecimal couponAmount;

	// 管理员后台调整订单使用的折扣金额
    private BigDecimal discountAmount;

	// 自动确认时间（天）
    private Integer autoConfirmDay;

	// 可以获得的积分
    private Integer integration;

	// 可以获得的成长值
    private Integer growth;

	// 活动信息
    private String promotionInfo;

	// 发票类型：0->不开发票；1->电子发票；2->纸质发票
    private String billType;

	// 发票抬头
    private String billHeader;

	// 发票内容
    private String billContent;

	// 收票人电话
    private String billReceiverPhone;

	// 收票人邮箱
    private String billReceiverEmail;

    // 收货人
    private String receiverName;

    // 收货人电话
    private String receiverPhone;

    // 邮编
    private String receiverPostCode;

    // 收货人省份
    private String receiverProvince;

    // 收货人城市
    private String receiverCity;

    // 收货人区域
    private String receiverRegion;

    // 收货人详细地址
    private String receiverDetailAddress;

	// 是否积分产品
    private String isIntegral;

	// 订单备注
    private String orderRemark;

    // 后台备注
    private String note;

    @TableField(exist = false)
    private List<OrderDetail> orderDetails;

    @TableField(exist = false)
    private List<OrderOperateLog> orderOperateLogs;

	@TableField(exist = false)
    private String statusStr;

    @TableField(exist = false)
    private CustomerAddress customerAddress;

    @TableField(exist = false)
    private Customer customer;

    /**
     * 待支付剩余时间
     */
    @TableField(exist = false)
    private long unPayTime;

}
