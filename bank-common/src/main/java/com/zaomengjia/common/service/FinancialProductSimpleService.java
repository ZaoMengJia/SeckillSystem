package com.zaomengjia.common.service;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.constant.RedisKey;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.utils.RedisUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 01:14
 */
@Service
@Transactional
@CacheConfig(cacheNames = "financial-product")
public class FinancialProductSimpleService {

    private final FinancialProductMapper financialProductMapper;

    private final RedisUtils redisUtils;

    public FinancialProductSimpleService(FinancialProductMapper financialProductMapper, RedisUtils redisUtils) {
        this.financialProductMapper = financialProductMapper;
        this.redisUtils = redisUtils;
    }

    public FinancialProduct getFinancialProductEntity(String financialProductId) {
        String key = RedisKey.financialProductIdKey(financialProductId);
        JSONObject o = ((JSONObject) redisUtils.get(key));
        if(o == null) {
            FinancialProduct financialProduct = financialProductMapper.findById(financialProductId).orElse(null);
            if(financialProduct == null) {
                return null;
            }

            redisUtils.set(key, financialProduct);
            return financialProduct;
        }
        else {
            return o.toJavaObject(FinancialProduct.class);
        }
    }

    public List<FinancialProduct> getCacheByList(List<String> idList) {
        List<String> keyList = idList.stream().map(RedisKey::financialProductIdKey).collect(Collectors.toList());
        List<Object> object = redisUtils.multiGet(keyList);
        if(object != null) {
            List<FinancialProduct> resultList = object.stream().map(c -> ((JSONObject) c).toJavaObject(FinancialProduct.class)).collect(Collectors.toList());

            if(resultList.size() == idList.size()) {
                return resultList;
            }
        }

        List<FinancialProduct> productList = financialProductMapper.findAllById(idList);
        if(productList.size() == 0) {
            return new ArrayList<>();
        }

        Map<String, Object> keyProductMap = productList.stream().collect(Collectors.toMap(c -> RedisKey.financialProductIdKey(c.getId()), c -> c));
        redisUtils.multiSet(keyProductMap);
        return productList;
    }

    public void setCache(List<FinancialProduct> list) {
        Map<String, Object> keyObjectMap = list.stream().collect(Collectors.toMap(
                c -> RedisKey.financialProductIdKey(c.getId()),
                c -> c
        ));
        redisUtils.multiSet(keyObjectMap);
    }
}
