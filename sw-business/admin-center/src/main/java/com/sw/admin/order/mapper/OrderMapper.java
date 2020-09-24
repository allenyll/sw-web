package com.sw.admin.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sw.common.entity.order.Order;

import java.util.List;
import java.util.Map;

/**
 * 订单基础信息表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-26 21:11:07
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 获取未支付订单数量
     * @param params
     * @return
     */
    Integer getUnPayNum(Map<String, Object> params);

    /**
     * 获取待发货订单数量
     * @param params
     * @return
     */
    Integer getUnReceiveNum(Map<String, Object> params);

    /**
     * 获取待收货订单数量
     * @param params
     * @return
     */
    Integer getDeliveryNum(Map<String, Object> params);

    /**
     * 获取已收货订单数量
     * @param params
     * @return
     */
    Integer getReceiveNum(Map<String, Object> params);


    /**
     * 获取已评价订单数量
     * @param params
     * @return
     */
    Integer getAppraisesNum(Map<String, Object> params);

    /**
     * 获取已完成订单数量
     * @param params
     * @return
     */
    Integer getFinishNum(Map<String, Object> params);

    /**
     * 获取订单总数
     * @param params
     * @return
     */
    Integer selectCount(Map<String, Object> params);

    /**
     * 分页查询订单
     * @param params
     * @return
     */
    List<Map<String, Object>>  getOrderPage(Map<String, Object> params);
}
