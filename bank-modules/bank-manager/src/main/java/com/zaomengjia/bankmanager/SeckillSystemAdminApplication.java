package com.zaomengjia.bankmanager;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.zaomengjia.common", "com.zaomengjia.bankmanager"})
//@MapperScan("com.zaomengjia.common.*")
@EnableJpaRepositories(basePackages = "com.zaomengjia.common.dao")
@EntityScan(basePackages = "com.zaomengjia.common.pojo")
public class SeckillSystemAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillSystemAdminApplication.class,args);
    }
}
