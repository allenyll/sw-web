package com.sw.admin.order.consumer;

import com.sw.common.util.MapUtil;
import com.sw.admin.order.service.impl.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    IOrderService orderService;

    @RabbitListener(queues = "sw.order.cancel")
    public void handle(HashMap<String, Object> map) {
        LOGGER.info("receive delay message orderId:{}", MapUtil.getString(map, "orderId"));
        map.put("note", "支付超时自动取消");
        map.put("opt_name", "rabbit");
        orderService.cancelOrder(map);
    }
}
