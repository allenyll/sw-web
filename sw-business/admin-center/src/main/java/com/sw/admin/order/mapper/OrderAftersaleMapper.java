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
     * 查询售后申请单总量
     * @param orderQueryDto 查询条件
     * @return 数量
     */
    int selectCount(OrderQueryDto orderQueryDto);

    /**
     * 分页查询售后申请单
     * @param orderQueryDto 查询条件
     * @return 售后申请单集合
     */
    List<OrderAftersaleDto> getOrderAftersalePage(OrderQueryDto orderQueryDto);

    /**
     * 售后服务单集合查询
     * @param queryDto 查询条件
     * @return 售后服务单集合
     */
    List<OrderAftersaleDto> selectAftersaleList(OrderQueryDto queryDto);

    /**
     * 根据售后单ID获取申请单详情
     * @param id 售后单ID
     * @return 申请单详情
     */
    OrderAftersaleDto getApplyById(Long id);
}
