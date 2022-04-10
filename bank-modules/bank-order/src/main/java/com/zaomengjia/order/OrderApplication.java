package com.zaomengjia.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 14:19
 */
@SpringBootApplication(scanBasePackages = {"com.zaomengjia.common", "com.zaomengjia.order"})
@EnableDiscoveryClient
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
