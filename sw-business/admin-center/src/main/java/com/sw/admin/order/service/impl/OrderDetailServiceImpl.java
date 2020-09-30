package com.sw.admin.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sw.admin.member.service.impl.CustomerAddressServiceImpl;
import com.sw.admin.member.service.impl.CustomerServiceImpl;
import com.sw.client.feign.CustomerFeignClient;
import com.sw.common.entity.customer.Customer;
import com.sw.common.entity.customer.CustomerAddress;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderDetail;
import com.sw.common.entity.order.OrderOperateLog;
import com.sw.common.util.DateUtil;
import com.sw.common.util.MapUtil;
import com.sw.admin.order.mapper.OrderDetailMapper;
import com.sw.admin.order.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单明细表
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2019-06-26 21:15:58
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    CustomerServiceImpl customerService;

    @Autowired
    CustomerAddressServiceImpl customerAddressService;

    @Autowired
    OrderOperateLogServiceImpl orderOperateLogService;

    public List<OrderDetail> getOrderDetailList(Map<String, Object> params){
        QueryWrapper<OrderDetail> wrapper  = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("ORDER_ID", MapUtil.getString(params, "orderId"));
        List<OrderDetail> list = orderDetailMapper.selectList(wrapper);
        return list;
    }

    public Order getOrderDetail(Map<String, Object> map) {
        QueryWrapper<Order> wrapper  = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("ID", MapUtil.getString(map, "orderId"));
        Order order = orderService.getOne(wrapper);
        if(order == null){
            return null;
        }
        String addTime = order.getOrderTime();
        Date addDate = DateUtil.stringToDate(addTime, "yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date();
        long unPayTime = addDate.getTime() + 30 * 60 * 1000 - nowDate.getTime() ;
        order.setUnPayTime(unPayTime);
        Customer customer = customerService.getById(order.getCustomerId());
        order.setCustomer(customer);
        CustomerAddress customerAddress = customerAddressService.getById(order.getAddressId());
        order.setCustomerAddress(customerAddress);
        List<OrderDetail> orderDetailList = getOrderDetailList(map);
        order.setOrderDetails(orderDetailList);
        List<OrderOperateLog> orderOperateLogs = orderOperateLogService.getOperateList(map);
        order.setOrderOperateLogs(orderOperateLogs);
        return order;
    }

}
