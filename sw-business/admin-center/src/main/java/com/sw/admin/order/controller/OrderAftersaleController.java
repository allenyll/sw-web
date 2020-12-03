package com.sw.admin.order.controller;

import com.sw.admin.order.service.IOrderService;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.admin.order.service.impl.OrderAftersaleServiceImpl;
import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderAftersale;
import com.sw.common.entity.system.User;
import com.sw.common.util.DataResponse;
import com.sw.common.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @ApiOperation("分页查询售后申请单")
    @RequestMapping(value = "pageList", method = RequestMethod.POST)
    public Result<Map<String, Object>> pageList(@RequestBody OrderQueryDto orderQueryDto){
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> map = new HashMap<>();
        int total = service.selectCount(orderQueryDto);
        List<OrderAftersaleDto>  orderList = service.getOrderAftersalePage(orderQueryDto);
        map.put("total", total);
        map.put("list", orderList);
        result.setObject(map);
        return result;
    }

    @ApiOperation("获取申请单详情")
    @ResponseBody
    @RequestMapping(value = "/getDetail/{id}", method = RequestMethod.GET)
    public Result<OrderAftersaleDto> getDetail(@CurrentUser(isFull = true) User user, @PathVariable Long id){
        return service.getDetail(user, id);
    }

    @ApiOperation("更新申请单详情")
    @ResponseBody
    @RequestMapping(value = "/updateAftersaleStatus", method = RequestMethod.POST)
    public Result updateAftersaleStatus(@CurrentUser(isFull = true) User user, @RequestBody OrderAftersaleDto aftersaleDto){
        return service.updateAftersaleStatus(user, aftersaleDto);
    }

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

    @ApiOperation("提交售后申请单")
    @RequestMapping(value = "/submitOrderAftersale", method = RequestMethod.POST)
    public Result<OrderAftersaleDto> submitOrderAftersale(@RequestBody OrderAftersaleDto orderAftersaleDto){
        return service.submitOrderAftersale(orderAftersaleDto);
    }

    @ApiOperation("保存发货单")
    @RequestMapping(value = "/saveDeliveryInfo", method = RequestMethod.POST)
    public Result<OrderAftersaleDto> saveDeliveryInfo(@RequestBody OrderAftersaleDto orderAftersaleDto){
        return service.saveDeliveryInfo(orderAftersaleDto);
    }

    @ApiOperation("取消售后申请单")
    @RequestMapping(value = "/cancelOrderAftersale", method = RequestMethod.POST)
    public Result<OrderAftersaleDto> cancelOrderAftersale(@CurrentUser(isFull = true) User user, @RequestBody OrderAftersaleDto orderAftersaleDto){
        return service.cancelOrderAftersale(user, orderAftersaleDto);
    }

    @ApiOperation("删除售后申请单")
    @RequestMapping(value = "/deleteOrderAftersale", method = RequestMethod.POST)
    public Result<OrderAftersaleDto> deleteOrderAftersale(@CurrentUser(isFull = true) User user, @RequestBody OrderAftersaleDto orderAftersaleDto){
        return service.deleteOrderAftersale(user, orderAftersaleDto);
    }

}
