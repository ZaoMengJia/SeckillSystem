package com.zaomengjia;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.zaomengjia.dao")
public class SeckillSystemAdminApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SeckillSystemAdminApplication.class,args);
    }
}
