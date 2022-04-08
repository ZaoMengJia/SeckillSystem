package com.zaomengjia.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/1 01:34
 */
@SpringBootApplication(scanBasePackages = {"com.zaomengjia.common", "com.zaomengjia.auth"})
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
