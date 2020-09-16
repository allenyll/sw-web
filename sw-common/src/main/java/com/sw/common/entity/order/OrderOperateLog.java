package com.sw.common.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sw.common.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;


/**
 * 记录订单操作日志
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-07-16 16:34:06
 */
@Data
@TableName("snu_order_operate_log")
public class OrderOperateLog extends BaseEntity<OrderOperateLog> {

	private static final long serialVersionUID = 1L;

	// 操作日志主键
    @TableId(type = IdType.ASSIGN_UUID)
    private String pkOperateId;

	// 订单ID
    private String fkOrderId;

	// 订单状态
    private String orderStatus;

	//
    private String remark;

	@Override
    protected Serializable pkVal() {
		return pkOperateId;
	}



}
