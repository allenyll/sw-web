package com.sw.common.dto;

import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderDetail;
import lombok.Data;

import java.util.List;

/**
 * @Description:  订单相关返回DTO
 * @Author:       allenyll
 * @Date:         2020/11/11 11:21 上午
 * @Version:      1.0
 */
@Data
public class OrderReturnDto {

    /**
     * 订单
     */
    private Order order;

    /**
     * 订单明细
     */
    private OrderDetail orderDetail;

    /**
     * 订单集合
     */
    private List<Order> orderList;

    /**
     * 售后服务单集合
     */
    private List<OrderAftersaleDto> orderRefundList;
}
