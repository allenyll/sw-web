package com.sw.mq.config;

import com.sw.mq.constants.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * 订单实际消费的交换机
     * @return
     */
    @Bean
    public DirectExchange orderDirectExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange()).durable(true).build();
    }

    /**
     * 订单延迟队列的交换机
     * @return
     */
    @Bean
    public DirectExchange orderTTLDirectExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getExchange()).durable(true).build();
    }

    /**tyop
     * 订单实际消费队列
     */
    @Bean
    public Queue orderQueue() {
        return new Queue(QueueEnum.QUEUE_ORDER_CANCEL.getQueueName());
    }

    /**
     * 订单延迟队列（死信队列）
     * @return
     */
    @Bean
    public Queue orderTTLQueue() {
        return QueueBuilder
                .durable(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getQueueName())
                .withArgument("x-dead-letter-exchange", QueueEnum.QUEUE_ORDER_CANCEL.getExchange())     //
                .withArgument("x-dead-letter-routing-key", QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey())
                .build();
    }

    /**
     * 将订单队列绑定到交换机
     * @param orderDirectExchange
     * @param orderQueue
     * @return
     */
    @Bean
    public Binding orderBinding(DirectExchange orderDirectExchange, Queue orderQueue) {
        return BindingBuilder.bind(orderQueue).to(orderDirectExchange).with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    }

    /**
     * 将订单队列绑定到交换机
     * @param orderTTLDirectExchange
     * @param orderTTLQueue
     * @return
     */
    @Bean
    public Binding orderTTLBinding(DirectExchange orderTTLDirectExchange, Queue orderTTLQueue) {
        return BindingBuilder.bind(orderTTLQueue).to(orderTTLDirectExchange).with(QueueEnum.QUEUE_ORDER_CANCEL_TTL.getRouteKey());
    }


}
