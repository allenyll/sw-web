package com.sw.common.entity.customer;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-09
 */
@TableName("snu_customer_point")
@ToString
@Data
public class CustomerPoint extends BaseEntity<CustomerPoint> {

    private static final long serialVersionUID = 1L;

    /**
     * 积分主键ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
	private String pkPointId;
    /**
     * 用户id
     */
	private String fkCustomerId;

	/**
	 * 用户名称
	 */
	@TableField(exist = false)
	private String customerName;

	/**
	 * 用户账户
	 */
	@TableField(exist = false)
	private String customerAccount;

    /**
     * 积分
     */
	private Integer point;
    /**
     * 使用积分
     */
	private Integer used;

	@Override
	protected Serializable pkVal() {
		return this.pkPointId;
	}

}
