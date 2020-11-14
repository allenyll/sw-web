package com.sw.admin.order.mapper;

import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.entity.order.OrderAftersale;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 售后申请
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-10 17:36:28
 */
public interface OrderAftersaleMapper extends BaseMapper<OrderAftersale> {

    /**
     * 售后服务单集合查询
     * @param queryDto
     * @return
     */
    List<OrderAftersaleDto> selectAftersaleList(OrderQueryDto queryDto);
}
