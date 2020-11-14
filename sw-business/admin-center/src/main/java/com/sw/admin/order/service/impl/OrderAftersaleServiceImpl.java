package com.sw.admin.order.service.impl;

import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.common.entity.order.OrderAftersale;
import com.sw.admin.order.mapper.OrderAftersaleMapper;
import com.sw.admin.order.service.IOrderAftersaleService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 售后申请
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-10 17:36:28
 */
@Service("orderAftersaleService")
public class OrderAftersaleServiceImpl extends ServiceImpl<OrderAftersaleMapper,OrderAftersale> implements IOrderAftersaleService{

    /**
     * 最大可退金额=需退款商品原价(30元)-订单中优惠的金额（20元）*（需退款商品原价/订单原价）（30元/100元）= 24元
     */

    @Resource
    private OrderAftersaleMapper orderAftersaleMapper;

    @Override
    public List<OrderAftersaleDto> getOrderRefundList(OrderQueryDto queryDto) {
        List<OrderAftersaleDto> list = orderAftersaleMapper.selectAftersaleList(queryDto);
        return list;
    }
}
