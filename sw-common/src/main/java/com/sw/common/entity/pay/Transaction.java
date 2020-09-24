package com.sw.common.entity.pay;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 交易表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-04-04 16:37:41
 */
@Data
@TableName("snu_transaction")
public class Transaction extends BaseEntity<Transaction> {

	private static final long serialVersionUID = 1L;

	// 交易主键
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

	// 交易单号
    private String transactionNo;

	// 交易人
    private Long customerId;

    private Long orderId;

	// 交易金额
    private BigDecimal amount;

	// 使用的积分
    private Integer integral;

	// 支付渠道
    private String payChannel;

	// 支付来源
    private String source;

	// 交易状态 SW1201 未完成  SW1202 已完成 SW1203 取消 SW1204 异常
    private String status;

	// 支付时间
    private String transactionTime;

	// 备注
    private String remark;

}
