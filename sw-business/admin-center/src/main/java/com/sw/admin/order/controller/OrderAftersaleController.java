package com.sw.admin.order.controller;

import com.sw.admin.order.service.IOrderService;
import com.sw.client.controller.BaseController;
import com.sw.admin.order.service.impl.OrderAftersaleServiceImpl;
import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderAftersale;
import com.sw.common.util.DataResponse;
import com.sw.common.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 售后申请
 *
 * @author allenyll
 * @email 806141743@qq.com
 * @date 2020-11-10 17:36:28
 */
@Slf4j
@Api(value = "售后申请", tags = "售后申请")
@RestController
@RequestMapping("/orderAftersale")
public class OrderAftersaleController extends BaseController<OrderAftersaleServiceImpl,OrderAftersale> {

    @Autowired
    IOrderService orderService;

    @ApiOperation("获取订单及售后申请单列表")
    @RequestMapping(value = "/getOrderRefundList", method = RequestMethod.POST)
    public Result<Map<String, Object>> getOrderRefundList(@RequestBody OrderQueryDto queryDto){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> map =  new HashMap<>();

        List<Order> orderList = orderService.getOrderList(queryDto);

        List<OrderAftersaleDto> orderRefundList = service.getOrderRefundList(queryDto);

        map.put("orderList", orderList);
        map.put("orderRefundList", orderRefundList);
        result.setObject(map);
        return result;
    }

}
