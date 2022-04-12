package com.zaomengjia.stock.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 04:00
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public ThreadPoolExecutor asyncServiceExecutor() {
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
