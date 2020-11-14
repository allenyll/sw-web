package com.sw.admin.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.entity.order.OrderAftersale;

import java.util.List;
import java.util.Map;

/**
 * 售后申请
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-10 17:36:28
 */
public interface IOrderAftersaleService extends IService<OrderAftersale> {

    /**
     * 查询售后服务单
     * @param queryDto
     * @return
     */
    List<OrderAftersaleDto> getOrderRefundList(OrderQueryDto queryDto);
}
