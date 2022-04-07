package com.zaomengjia.common.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 01:25
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.zaomengjia.common.dao")
@EntityScan(basePackages = "com.zaomengjia.common.pojo")
public class JpaConfig {
}
