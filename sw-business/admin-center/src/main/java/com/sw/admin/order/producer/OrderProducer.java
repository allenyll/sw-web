package com.sw.admin.order.producer;

import com.sw.common.util.MapUtil;
import com.sw.mq.constants.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(Map<String, Object> param, long delayTimes){
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getExchange(), QueueEnum.QUEUE_ORDER_CANCEL_TTL.getRouteKey(), param, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
            return message;
        });
        LOGGER.info("send delay message orderId:{}", MapUtil.getString(param, "orderId"));
    }

}
