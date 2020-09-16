package com.sw.common.entity.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 会员余额明细表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-04-10 16:24:29
 */
@Data
@TableName("snu_customer_balance_detail")
public class CustomerBalanceDetail extends BaseEntity<CustomerBalanceDetail> {

	private static final long serialVersionUID = 1L;

	//
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkBalanceDetail;

	//
    private String fkCustomerId;

	// 使用金额
    private BigDecimal balance;

    private String type;

	// 使用时间
    private String time;

	// 使用状态
    private String status;

    private String remark;

	// 充值来源
    private String channelId;

	@Override
    protected Serializable pkVal() {
		return pkBalanceDetail;
	}



}
