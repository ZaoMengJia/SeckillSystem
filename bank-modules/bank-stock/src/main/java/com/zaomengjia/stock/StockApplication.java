package com.zaomengjia.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 01:24
 */

@SpringBootApplication(scanBasePackages = {"com.zaomengjia.common", "com.zaomengjia.stock"})
@EnableDiscoveryClient
public class StockApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }
}
