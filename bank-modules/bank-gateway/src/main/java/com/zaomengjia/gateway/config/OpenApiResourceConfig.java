package com.zaomengjia.gateway.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 02:59
 */
@RestController
public class OpenApiResourceConfig {

    private final DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    private String currentServiceName;

    @Value("${spring.application.url}")
    private String url;

    @Value("classpath:static/swagger-config.json")
    private Resource resource;

    public OpenApiResourceConfig(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/swagger-config.json")
    public Map<String, Object> swaggerConfig() {
        Map<String, Object> config = new LinkedHashMap<>();
        List<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = new LinkedList<>();

        Map<String, String> serviceNameContextPathMap = new HashMap<>();
        serviceNameContextPathMap.put("bank-auth", "");
        serviceNameContextPathMap.put("bank-manager", "/web");
        serviceNameContextPathMap.put("bank-order", "/weixin");

        discoveryClient.getServices().forEach(serviceName ->
                discoveryClient.getInstances(serviceName).forEach(serviceInstance ->{
                            if(serviceNameContextPathMap.containsKey(serviceName)) {
                                String prefix = Optional.ofNullable(serviceNameContextPathMap.get(serviceName)).orElse("");
                                urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(serviceName, serviceInstance.getUri() + prefix + "/v3/api-docs"));
                            }
                            else {
                                if(serviceName.equals(currentServiceName)) {
                                    urls.add(new AbstractSwaggerUiConfigProperties.SwaggerUrl(serviceName, serviceInstance.getUri() + "/v3/api-docs-gateway"));
                                }
                            }
                        }
                )
        );
        config.put("urls", urls);
        return config;
    }

    @SneakyThrows
    @GetMapping("/v3/api-docs-gateway")
    public JSONObject getCurrentSwaggerConfig() {
        byte[] bytes = new byte[1024 * 1024 * 2];
        IOUtils.read(resource.getInputStream(), bytes);
        return JSON.parseObject(new String(bytes));
    }


//    @SneakyThrows
//    private String getSwaggerConfig() {
//        BufferedReader br = null;
//        String path = "/static/swagger-config.json";
//        br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path))));
//        String line = br.readLine();
//        StringBuilder sb = new StringBuilder();
//        while (line != null) {
//            sb.append(line + "\r\n");
//            line = br.readLine();
//        }
//        return sb.toString();
//    }



}
