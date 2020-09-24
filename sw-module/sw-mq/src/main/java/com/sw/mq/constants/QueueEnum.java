package com.sw.mq.constants;

import lombok.Getter;

/**
 * @Description:  rabbit消息队列配置类
 * @Author:       allenyll
 * @Date:         2020-03-30 17:16
 * @Version:      1.0
 */
@Getter
public enum QueueEnum {

    /**
     * 订单消息队列关键词
     */
    QUEUE_ORDER_CANCEL("sw.order.direct", "sw.order.cancel", "sw.order.cancel"),

    /**
     * 订单消息队列ttl
     */
    QUEUE_ORDER_CANCEL_TTL("sw.order.direct.ttl", "sw.order.cancel.ttl", "sw.order.cancel.ttl");


    private String exchange;

    private String queueName;

    private String routeKey;

    QueueEnum(String exchange, String queueName, String routeKey){
        this.exchange = exchange;
        this.queueName = queueName;
        this.routeKey = routeKey;
    }


}
