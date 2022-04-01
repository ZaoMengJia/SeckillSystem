package com.zaomengjia.bankmanager;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
@MapperScan("com.zaomengjia.common.*")
public class SeckillSystemAdminApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SeckillSystemAdminApplication.class,args);
        Environment env = context.getEnvironment();
        log.info("====================================================================");
        log.info("项目版本: {}", env.getProperty("project.version"));
        log.info("启动端口: {}", env.getProperty("server.port"));
        log.info("Startup complete ...");
        log.info("====================================================================");
    }
}
