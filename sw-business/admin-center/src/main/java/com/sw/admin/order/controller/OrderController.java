package com.sw.admin.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sw.client.annotion.CurrentUser;
import com.sw.client.controller.BaseController;
import com.sw.common.constants.dict.OrderStatusDict;
import com.sw.common.dto.OrderQueryDto;
import com.sw.common.dto.OrderReturnDto;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.order.service.impl.IOrderService;
import com.sw.admin.order.service.impl.OrderServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api("订单接口")
@RestController
@RequestMapping("order")
public class OrderController extends BaseController<OrderServiceImpl, Order> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    IOrderService orderService;

    @Override
    @ApiOperation("分页查询订单")
    @RequestMapping(value = "page", method = RequestMethod.GET)
    public DataResponse page(@RequestParam Map<String, Object> params){
        Map<String, Object> result = new HashMap<>();
        int total = orderService.selectCount(params);
        List<Map<String, Object>>  orderList = orderService.getOrderPage(params);
        result.put("total", total);
        result.put("list", orderList);
        return DataResponse.success(result);
    }

    @ApiOperation("删除订单")
    @RequestMapping(value = "deleteOrder", method = RequestMethod.POST)
    public DataResponse deleteOrder(@CurrentUser(isFull = true) User user, @RequestParam Map<String, Object> params){
        LOGGER.info("deleteOrder=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.deleteOrder(params);
        return dataResponse;
    }

    @ApiOperation("订单发货")
    @RequestMapping(value = "deliveryOrder", method = RequestMethod.POST)
    public DataResponse deliveryOrder(@CurrentUser(isFull = true) User user, @RequestParam Map<String, Object> params){
        LOGGER.info("deliveryOrder=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.deliveryOrder(params);
        return dataResponse;
    }

    @ApiOperation("关闭订单")
    @RequestMapping(value = "closeOrder", method = RequestMethod.POST)
    public DataResponse closeOrder(@CurrentUser(isFull = true) User user, @RequestParam Map<String, Object> params){
        LOGGER.info("closeOrder=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.closeOrder(params);
        return dataResponse;
    }

    @ApiOperation("修改订单金额")
    @RequestMapping(value = "updateMoneyInfo", method = RequestMethod.POST)
    public DataResponse updateMoneyInfo(@CurrentUser(isFull = true) User user, @RequestParam Map<String, Object> params){
        LOGGER.info("updateMoneyInfo=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.updateMoneyInfo(params);
        return dataResponse;
    }

    @ApiOperation("更新收货信息")
    @RequestMapping(value = "updateReceiverInfo", method = RequestMethod.POST)
    public DataResponse updateReceiverInfo(@CurrentUser(isFull = true) User user, @RequestParam Map<String, Object> params){
        LOGGER.info("updateReceiverInfo=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.updateReceiverInfo(params);
        return dataResponse;
    }

    @ApiOperation("修改订单备注")
    @RequestMapping(value = "updateOrderNote", method = RequestMethod.POST)
    public DataResponse updateOrderNote(@CurrentUser(isFull = true) User user, @RequestParam Map<String, Object> params){
        LOGGER.info("updateOrderNote=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.updateOrderNote(params);
        return dataResponse;
    }

    @ApiOperation("创建订单")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public DataResponse createOrder(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> param){
        Map<String, Object> result = new HashMap<>();
        param.put("userId", user.getId());
        log.info("缓存订单参数: {}"+param);
        Order order = new Order();
        try {
            orderService.createOrder(order, param);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponse.fail("创建订单失败!");
        }

        result.put("order", order);

        return DataResponse.success(result);
    }

    @ApiOperation("获取各类型订单数量")
    @RequestMapping(value = "/getOrderNum", method = RequestMethod.POST)
    public DataResponse getOrderNum(@RequestBody Map<String, Object> params){
        Map<String, Object> result =  new HashMap<>();

        int unPayNum = orderService.getUnPayNum(params);
        int unReceiveNum = orderService.getUnReceiveNum(params);
        int deliveryNum = orderService.getDeliveryNum(params);
        int receiveNum = orderService.getReceiveNum(params);
        int appraisesNum = orderService.getAppraisesNum(params);
        int finishNum = orderService.getFinishNum(params);
        result.put("unPayNum", unPayNum);
        result.put("unReceiveNum", unReceiveNum);
        result.put("deliveryNum", deliveryNum);
        result.put("receiveNum", receiveNum);
        result.put("appraisesNum", appraisesNum);
        result.put("finishNum", finishNum);
        return DataResponse.success(result);
    }

    @ApiOperation("全量获取订单")
    @RequestMapping(value = "/getAllOrderList", method = RequestMethod.POST)
    public DataResponse getAllOrderList(@RequestBody Map<String, Object> params){
        Map<String, Object> result =  new HashMap<>();
        page = Integer.parseInt(params.get("page").toString());
        limit = Integer.parseInt(params.get("limit").toString());

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETE", 0);
        wrapper.eq("ORDER_TYPE", "SW0601");
        wrapper.eq("CUSTOMER_ID", MapUtil.getString(params, "customerId"));

        int total = service.count(wrapper);
        Page<Order> list = service.page(new Page<>(page, limit), wrapper);

        result.put("total", total);
        result.put("list", list.getRecords());

        return DataResponse.success(result);
    }

    @ApiOperation("获取订单列表")
    @RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
    public DataResponse getOrderList(@RequestBody OrderQueryDto queryDto){
        Map<String, Object> result =  new HashMap<>();

        List<Order> list = orderService.getOrderList(queryDto);

        result.put("list", list);

        return DataResponse.success(result);
    }

    @ApiOperation("取消订单小程序")
    @RequestMapping(value = "cancelMiniOrder", method = RequestMethod.POST)
    public DataResponse cancelMiniOrder(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> params){
        LOGGER.info("cancelOrder=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.cancelOrder(params);
        return dataResponse;
    }

    @ApiOperation("取消订单")
    @RequestMapping(value = "cancelOrder", method = RequestMethod.POST)
    public DataResponse cancelOrder(@CurrentUser(isFull = true) User user, @RequestParam Map<String, Object> params){
        LOGGER.info("cancelOrder=>params："+params);
        params.put("userId", user.getId());
        DataResponse dataResponse = orderService.cancelOrder(params);
        return dataResponse;
    }

    @ApiOperation("删除订单")
    @RequestMapping(value = "/deleteOrder/{orderId}", method = RequestMethod.POST)
    public DataResponse deleteOrder(@PathVariable String orderId){
        if(StringUtil.isEmpty(orderId)){
            return DataResponse.fail("订单不存在!");
        }
        try {
            Order order = orderService.getById(orderId);
            order.setIsDelete(1);
            order.setUpdateTime(DateUtil.getCurrentDateTime());
            orderService.updateById(order);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponse.fail("订单删除失败");
        }

        return DataResponse.success();
    }

    @ApiOperation("订单收货")
    @RequestMapping(value = "/receiveOrder/{orderId}", method = RequestMethod.POST)
    public DataResponse receiveOrder(@PathVariable String orderId){
        if(StringUtil.isEmpty(orderId)){
            return DataResponse.fail("订单不存在!");
        }
        String time = DateUtil.getCurrentDateTime();
        try {
            Order order = orderService.getById(orderId);
            order.setOrderStatus(OrderStatusDict.RECEIVE.getCode());
            order.setReceiveTime(time);
            order.setUpdateTime(time);
            orderService.updateById(order);
        } catch (Exception e) {
            e.printStackTrace();
            return DataResponse.fail("更新订单状态已收货失败");
        }

        return DataResponse.success();
    }

    @ApiOperation("根据订单ID获取订单")
    @RequestMapping(value = "/selectById", method = RequestMethod.POST)
    public Order selectById(@RequestParam String orderId) {
        return service.getById(orderId);
    }

    @ApiOperation("更新订单支付状态")
    @RequestMapping(value = "/updateById", method = RequestMethod.POST)
    public void updateById(@RequestBody Order order) {
        service.updateById(order);
    }

    @ApiOperation("未支付订单放到消息队列")
    @ResponseBody
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public DataResponse sendMessage(@CurrentUser(isFull = true) User user, @RequestBody Map<String, Object> params) {
        params.put("userId", user.getId());
        DataResponse dataResponse = service.sendMessage(params);
        return dataResponse;
    }

    @ApiOperation("获取订单信息")
    @ResponseBody
    @RequestMapping(value = "/getOrderInfo", method = RequestMethod.POST)
    public Result<OrderReturnDto> getOrderInfo(@CurrentUser(isFull = true) User user, @RequestBody OrderQueryDto queryDto) {
        return service.getOrderInfo(user, queryDto);
    }

}
