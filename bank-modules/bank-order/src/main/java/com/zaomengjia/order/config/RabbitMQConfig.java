package com.zaomengjia.order.config;

import com.zaomengjia.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 00:48
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public Queue orderQueue() {
        return new Queue(RabbitMQConstant.ORDER_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RabbitMQConstant.DEFAULT_EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(orderQueue()).to(directExchange()).with(RabbitMQConstant.CREATE_ORDER_ROUTING_NAME);
    }

    @Bean
    public ThreadPoolExecutor rabbitMqAsyncServiceExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                16,
                300,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(50)
        );

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
