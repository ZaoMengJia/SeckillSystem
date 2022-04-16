package com.zaomengjia.common.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.utils.RedisUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.zaomengjia.common.constant.RedisKey.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 01:53
 */
@Service
public class StockSimpleService {
    private final RedisUtils redisUtils;

    private final SaleProductDetailSimpleService saleProductDetailSimpleService;

    public StockSimpleService(RedisUtils redisUtils, SaleProductDetailSimpleService saleProductDetailSimpleService) {
        this.redisUtils = redisUtils;
        this.saleProductDetailSimpleService = saleProductDetailSimpleService;
    }

    public void addTokenBucket(String financialProductId, String seckillActivityId, long total) {

        Set<String> tokenSet = LongStream.rangeClosed(1, total).mapToObj(c -> UUID.fastUUID().toString(true)).collect(Collectors.toSet());
        if(tokenSet.size() != total) {
            tokenSet = LongStream.rangeClosed(1, total).mapToObj(c -> UUID.fastUUID().toString(true)).collect(Collectors.toSet());
        }
        redisUtils.sSet(tokenBucketMapKey(financialProductId, seckillActivityId), tokenSet.toArray());
    }

    public void deleteTokenBucket(String financialProductId, String seckillActivityId) {
        redisUtils.del(tokenBucketMapKey(financialProductId, seckillActivityId));
    }

    public void deleteTokenBucketBatch(List<Pair<String, String>> list) {
        String[] keys = list.stream().map(c -> tokenBucketMapKey(c.getFirst(), c.getSecond())).toArray(String[]::new);
        redisUtils.del(keys);
    }

    public void updateTokenBucket(String financialProductId, String seckillActivityId, long total) {
        deleteTokenBucket(financialProductId, seckillActivityId);
        addTokenBucket(financialProductId, seckillActivityId, total);
    }

    public String getToken(String financialProductId, String seckillActivityId) {
        Set<Object> tokenSet = redisUtils.sGet(tokenBucketMapKey(financialProductId, seckillActivityId));
        if(tokenSet == null) {
            return null;
        }

        int size = tokenSet.size();
        if(size == 0) {
            return null;
        }

        if(size > 10) {
            int index = new Random().nextInt(size);
            return (String) tokenSet.toArray()[index];
        }
        else {
            return (String) tokenSet.toArray()[0];
        }
    }

    public String attemptGetToken(String financialProductId, String seckillActivityId) {
        return (String) redisUtils.sPop(tokenBucketMapKey(financialProductId, seckillActivityId));
    }

    public Set<String> getDirtyStockKeySet() {
        Set<Object> set = redisUtils.sGet(stockDirtyKey());
        if(set == null) {
            return null;
        }
        else {
            return set.stream().map(c -> (String) c).collect(Collectors.toSet());
        }
    }

    public boolean isDirtyStock(String financialProductId, String seckillActivityId) {
        return redisUtils.sHasKey(stockDirtyKey(), financialProductId + "::" + seckillActivityId);
    }

    public void setDirtyStock(String financialProductId, String seckillActivityId, boolean isDirty) {
        if(isDirty) {
            redisUtils.sSet(stockDirtyKey(), financialProductId + "::" + seckillActivityId);
        }
        else {
            redisUtils.setRemove(stockDirtyKey(), financialProductId + "::" + seckillActivityId);
        }
    }

    public long getStock(String financialProductId, String seckillActivityId) {
        Integer get = (Integer) redisUtils.get(stockMapKey(financialProductId, seckillActivityId));
        return get != null ? get : 0;
    }

    public void setStockAndUpdateDetail(String financialProductId, String seckillActivityId, long quantity) {
        setStock(financialProductId, seckillActivityId, quantity);

        SaleProductDetail detail = saleProductDetailSimpleService.findByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId);
        String key = saleProductDetailKey(detail.getId(), financialProductId, seckillActivityId);
        Object o = redisUtils.get(key);
        if(o != null) {
            JSONObject jsonObject = (JSONObject) o;
            SaleProductDetail saleProductDetail = jsonObject.toJavaObject(SaleProductDetail.class);
            saleProductDetail.setQuantity(quantity);
            redisUtils.set(key, saleProductDetail);
        }
    }

    public void setStock(String financialProductId, String seckillActivityId, long quantity) {
        redisUtils.set(stockMapKey(financialProductId, seckillActivityId), quantity);
    }

    public void deleteStock(String financialProductId, String seckillActivityId) {
        redisUtils.del(stockMapKey(financialProductId, seckillActivityId));
    }

    public void deleteStockBatch(List<Pair<String, String>> list) {
        redisUtils.del(list.stream().map(c -> stockMapKey(c.getFirst(), c.getSecond())).toArray(String[]::new));
    }

    public boolean decrStock(String financialProductId, String seckillActivityId, long quantity) {
        long newValue = redisUtils.decr(stockMapKey(financialProductId, seckillActivityId));
        setDirtyStock(financialProductId, seckillActivityId, true);
        if(newValue < 0) {
            //相当于事务回滚
            redisUtils.incr(stockMapKey(financialProductId, seckillActivityId), quantity);
            return false;
        }

        return true;

    }

    public boolean decrStockAndUpdateDetail(String saleProductId, String financialProductId, String seckillActivityId, long quantity) {
        long newValue = redisUtils.decr(stockMapKey(financialProductId, seckillActivityId));
        setDirtyStock(financialProductId, seckillActivityId, true);
        if(newValue < 0) {
            //相当于事务回滚
            redisUtils.incr(stockMapKey(financialProductId, seckillActivityId), quantity);
            return false;
        }

        String key = saleProductDetailKey(saleProductId, financialProductId, seckillActivityId);
        Object o = redisUtils.get(key);
        if(o != null) {
            JSONObject jsonObject = (JSONObject) o;
            SaleProductDetail saleProductDetail = jsonObject.toJavaObject(SaleProductDetail.class);
            saleProductDetail.setQuantity(newValue);
            redisUtils.set(key, saleProductDetail);
        }
        return true;
    }
}
