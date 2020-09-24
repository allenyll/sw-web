package com.sw.admin.order.controller;

import com.sw.client.controller.BaseController;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderDetail;
import com.sw.common.util.DataResponse;
import com.sw.common.util.StringUtil;
import com.sw.admin.order.service.impl.OrderDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("orderDetail")
public class OrderDetailController extends BaseController<OrderDetailServiceImpl, OrderDetail> {


    @Autowired
    OrderDetailServiceImpl orderDetailService;

    @ResponseBody
    @RequestMapping(value = "/getOrderDetail/{orderId}", method = RequestMethod.POST)
    public DataResponse getOrderDetail(@PathVariable Long orderId){
        Map<String, Object> result = new HashMap<>();
        if(StringUtil.isEmpty(orderId)){
            return DataResponse.fail("订单不存在!");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        try {
            Order order = orderDetailService.getOrderDetail(map);
            result.put("order", order);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponse.fail("获取订单失败");
        }

        return DataResponse.success(result);
    }
}
