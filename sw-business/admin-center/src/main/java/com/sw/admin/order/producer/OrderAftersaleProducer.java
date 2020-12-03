package com.sw.admin.order.producer;

import com.sw.common.dto.OrderAftersaleDto;
import com.sw.mq.constants.QueueEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description:  售后申请消息生产者
 * @Author:       allenyll
 * @Date:         2020/12/2 下午5:45
 * @Version:      1.0
 */
@Component
public class OrderAftersaleProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMessage(OrderAftersaleDto aftersaleDto, long delayTimes){
        amqpTemplate.convertAndSend(QueueEnum.QUEUE_APPLY_CANCEL_TTL.getExchange(), QueueEnum.QUEUE_APPLY_CANCEL_TTL.getRouteKey(), aftersaleDto, message -> {
            message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
            return message;
        });
    }

}
