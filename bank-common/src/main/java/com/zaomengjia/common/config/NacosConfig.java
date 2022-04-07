package com.zaomengjia.common.config;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 01:38
 */
@Configuration
public class NacosConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    @Primary
    public NacosDiscoveryProperties nacosProperties() {
        //获取公网ip
        String ip = getIp();
        logger.info("当前ip为{}", ip);

        NacosDiscoveryProperties nacosDiscoveryProperties = new NacosDiscoveryProperties();
//        nacosDiscoveryProperties.setIp(ip);
        return nacosDiscoveryProperties;
    }

    @SneakyThrows
    private String getIp() {
        String ip = null;
        String objWebURL = "https://bajiu.cn/ip/";
        BufferedReader br = null;
        try {
            URL url = new URL(objWebURL);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String s = "";
            String webContent = "";
            while ((s = br.readLine()) != null) {
                if (s.indexOf("互联网IP") != -1) {
                    ip = s.substring(s.indexOf("'") + 1, s.lastIndexOf("'"));
                    break;
                }
            }
        } finally {
            if (br != null)
                br.close();
        }
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException();
        }
        return ip;
    }
}
