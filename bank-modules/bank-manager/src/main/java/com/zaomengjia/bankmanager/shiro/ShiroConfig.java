package com.zaomengjia.bankmanager.shiro;


import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            @Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/admin/logout", "logout");
        filterMap.put("/admin/login","anon");
        filterMap.put("/admin/**","authc");
        filterMap.put("/order/**","authc");
        filterMap.put("/financialProduct/**","authc");
        filterMap.put("/seckillActivity/**","authc");
        filterMap.put("/saleProductDetail/**","authc");

        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(@Qualifier("adminRealm") AdminRealm adminRealm){
        return new DefaultWebSecurityManager(adminRealm);
    }

    @Bean
    public AdminRealm adminRealm(){
        return new AdminRealm();
    }
}
