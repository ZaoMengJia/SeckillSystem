package com.zaomengjia.bankmanager;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.zaomengjia.common", "com.zaomengjia.bankmanager"})
@MapperScan("com.zaomengjia.common.*")
public class SeckillSystemAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillSystemAdminApplication.class,args);
    }
}
