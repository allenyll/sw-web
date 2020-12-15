package com.sw.admin.order.consumer;

import com.sw.admin.order.service.IOrderAftersaleService;
import com.sw.common.dto.OrderAftersaleDto;
import com.sw.common.entity.system.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:  售后申请消息消费端
 * @Author:       allenyll
 * @Date:         2020/12/2 下午5:40
 * @Version:      1.0
 */
@Component
public class OrderAftersaleConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAftersaleConsumer.class);

    @Autowired
    IOrderAftersaleService orderAftersaleService;

    @RabbitListener(queues = "sw.apply.cancel")
    public void handle(OrderAftersaleDto aftersaleDto) {
        LOGGER.info("receive delay message id:{}", aftersaleDto.getId());
        User user = new User();
        user.setId(aftersaleDto.getDealUser());
        orderAftersaleService.cancelOrderAftersale(user, aftersaleDto);
    }
}
