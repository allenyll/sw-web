package com.sw.mq.config;

import com.sw.mq.constants.QueueEnum;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:  售后申请消息队列配置
 * @Author:       allenyll
 * @Date:         2020/12/2 下午5:26
 * @Version:      1.0
 */
@Configuration
public class RabbitMQApplyConfig {

    /**
     * 售后申请订单实际消费的交换机
     * @return 直连交换机
     */
    @Bean
    public DirectExchange applyDirectExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.QUEUE_APPLY_CANCEL.getExchange()).durable(true).build();
    }

    /**
     * 售后申请订单延迟队列的交换机
     * @return 延迟队列交换机
     */
    @Bean
    public DirectExchange applyTTLDirectExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(QueueEnum.QUEUE_APPLY_CANCEL_TTL.getExchange()).durable(true).build();
    }

    /**
     * 售后申请订单实际消费队列
     */
    @Bean
    public Queue applyQueue() {
        return new Queue(QueueEnum.QUEUE_APPLY_CANCEL.getQueueName());
    }

    /**
     * 售后申请订单延迟队列（死信队列）
     * @return 售后申请订单延迟队列
     */
    @Bean
    public Queue applyTTLQueue() {
        return QueueBuilder
                .durable(QueueEnum.QUEUE_APPLY_CANCEL_TTL.getQueueName())
                .withArgument("x-dead-letter-exchange", QueueEnum.QUEUE_APPLY_CANCEL.getExchange())
                .withArgument("x-dead-letter-routing-key", QueueEnum.QUEUE_APPLY_CANCEL.getRouteKey())
                .build();
    }

    /**
     * 将售后申请订单队列绑定到交换机
     * @param applyDirectExchange 交换机
     * @param applyQueue 消息队列
     * @return
     */
    @Bean
    public Binding applyBinding(DirectExchange applyDirectExchange, Queue applyQueue) {
        return BindingBuilder.bind(applyQueue).to(applyDirectExchange).with(QueueEnum.QUEUE_APPLY_CANCEL.getRouteKey());
    }

    /**
     * 将订单队列绑定到交换机
     * @param applyTTLDirectExchange 延迟队列
     * @param applyTTLQueue 延迟消息队列
     * @return
     */
    @Bean
    public Binding applyTTLBinding(DirectExchange applyTTLDirectExchange, Queue applyTTLQueue) {
        return BindingBuilder.bind(applyTTLQueue).to(applyTTLDirectExchange).with(QueueEnum.QUEUE_APPLY_CANCEL_TTL.getRouteKey());
    }

}
