package com.sw.common.entity.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 会员余额表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-04-10 16:16:16
 */
@Data
@TableName("snu_customer_balance")
public class CustomerBalance extends BaseEntity<CustomerBalance> {

	private static final long serialVersionUID = 1L;

	// 用户余额ID
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkBalanceId;

	//
    private String fkCustomerId;

	// 余额
    private BigDecimal balance;

	// 提现金额
    private BigDecimal withdrawCash;

	@Override
    protected Serializable pkVal() {
		return pkBalanceId;
	}



}
