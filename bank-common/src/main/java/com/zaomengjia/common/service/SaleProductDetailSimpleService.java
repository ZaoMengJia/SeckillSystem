package com.zaomengjia.common.service;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zaomengjia.common.constant.RedisKey.*;

/**
 * dao+缓存的服务
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 00:02
 */
@Service
public class SaleProductDetailSimpleService {

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final RedisUtils redisUtils;

    private final StockSimpleService stockSimpleService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SaleProductDetailSimpleService(SaleProductDetailMapper saleProductDetailMapper, RedisUtils redisUtils, @Lazy StockSimpleService stockSimpleService) {
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.redisUtils = redisUtils;
        this.stockSimpleService = stockSimpleService;
    }

    private String getKey(String id) {
        return (String) redisUtils.hget(saleProductDetailIdKeyMapKey(), id);
    }

    private String getKey(String activityId, String productId) {
        return (String) redisUtils.hget(saleProductDetailActivityIdProductIdKeyMapKey(), activityId + "::" + productId);
    }

    private void setKey(SaleProductDetail detail) {
        String key = saleProductDetailKey(detail.getId(), detail.getFinancialProductId(), detail.getSeckillActivityId());
        redisUtils.hset(saleProductDetailActivityIdProductIdKeyMapKey(), detail.getSeckillActivityId() + "::" + detail.getFinancialProductId(), key);
        redisUtils.hset(saleProductDetailIdKeyMapKey(), detail.getId(), key);
    }

    public SaleProductDetail findById(String id) {
        String key = getKey(id);
        SaleProductDetail detail;
        if(key == null) {
            Optional<SaleProductDetail> optional = saleProductDetailMapper.findById(id);
            if(!optional.isPresent()) {
                return null;
            }
            detail = optional.get();
            redisUtils.set(saleProductDetailKey(detail.getId(), detail.getFinancialProductId(), detail.getSeckillActivityId()), detail);
            setKey(detail);
        }
        else {
            detail = ((JSONObject)redisUtils.get(key)).toJavaObject(SaleProductDetail.class);
        }
        return detail;
    }

    public SaleProductDetail findByFinancialProductIdAndSeckillActivityId(String financialProductId, String seckillActivityId) {
        String key = getKey(seckillActivityId, financialProductId);
        SaleProductDetail detail;
        if(key == null) {
            logger.info("没有命中缓存，查找数据库 => {}::{}", financialProductId, seckillActivityId);
            detail = saleProductDetailMapper.findByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId);
            if(detail == null) {
                return null;
            }

            redisUtils.set(saleProductDetailKey(detail.getId(), detail.getFinancialProductId(), detail.getSeckillActivityId()), detail);
            setKey(detail);
        }
        else {
            detail = ((JSONObject)redisUtils.get(key)).toJavaObject(SaleProductDetail.class);
        }
        return detail;
    }

    public void setSeckillActivityIdProductMapBySeckillActivityId(String seckillActivityId, List<SaleProductDetail> detailList) {
        String setKey = saleProductDetailActivityIdKeyMap(seckillActivityId);
        List<String> saleProductDetailKeyList = detailList.stream().map(c -> saleProductDetailKey(c.getId(), c.getFinancialProductId(), c.getSeckillActivityId())).collect(Collectors.toList());

        redisUtils.del(setKey);
        if(saleProductDetailKeyList.size() > 0) {
            redisUtils.sSet(setKey, saleProductDetailKeyList.toArray());
        }
    }

    public List<SaleProductDetail> findBySeckillActivityId(String seckillActivityId) {
        String setKey = saleProductDetailActivityIdKeyMap(seckillActivityId);
        Set<Object> keySet = redisUtils.sGet(setKey);
        if(keySet != null) {
            List<String> keyList = keySet.stream().map(o -> (String) o).collect(Collectors.toList());
            List<Object> valueList = redisUtils.multiGet(keyList);
            if(valueList != null) {
                return valueList.stream().filter(Objects::nonNull).map(c -> ((JSONObject)c).toJavaObject(SaleProductDetail.class)).collect(Collectors.toList());
            }
        }

        List<SaleProductDetail> detailList = saleProductDetailMapper.findBySeckillActivityId(seckillActivityId);
        List<String> saleProductDetailKeyList = detailList.stream().map(c -> saleProductDetailKey(c.getId(), c.getFinancialProductId(), c.getSeckillActivityId())).collect(Collectors.toList());
        redisUtils.del(setKey);
        redisUtils.sSet(setKey, saleProductDetailKeyList.toArray());
        return detailList;
    }

    @Transactional
    public void save(SaleProductDetail detail) {
        detail = saleProductDetailMapper.save(detail);
        String key = saleProductDetailKey(detail.getId(), detail.getFinancialProductId(), detail.getSeckillActivityId());
        redisUtils.set(key, detail);
        setKey(detail);

        //设置缓存
        String setKey = saleProductDetailActivityIdKeyMap(detail.getSeckillActivityId());
        redisUtils.sSet(setKey, key);

        //重设库存
        stockSimpleService.updateTokenBucket(detail.getFinancialProductId(), detail.getSeckillActivityId(), detail.getQuantity());
        stockSimpleService.setStock(detail.getFinancialProductId(), detail.getSeckillActivityId(), detail.getQuantity());
    }


    @Transactional
    public void deleteByFinancialProductIdAndSeckillActivityId(String financialProductId, String seckillActivityId) {
        saleProductDetailMapper.deleteBySeckillActivityIdAndFinancialProductId(seckillActivityId, financialProductId);

        String key = getKey(seckillActivityId, financialProductId);
        if(key != null) {
            redisUtils.del(key);
            redisUtils.setRemove(saleProductDetailActivityIdKeyMap(seckillActivityId), key);

        }
        stockSimpleService.deleteStock(financialProductId, seckillActivityId);
        stockSimpleService.deleteTokenBucket(financialProductId, seckillActivityId);
    }

    public void modifyQuantityAndTotal(String financialProductId, String seckillActivityId, int quantity, int total) {
        saleProductDetailMapper.updateQuantityAndTotal(financialProductId, seckillActivityId, quantity, total);
        String key = getKey(seckillActivityId, financialProductId);
        JSONObject jsonObject = (JSONObject) redisUtils.get(key);
        if(jsonObject != null) {
            SaleProductDetail detail = jsonObject.toJavaObject(SaleProductDetail.class);
            detail.setQuantity(quantity);
            detail.setTotal(total);
            redisUtils.set(key, detail);
        }

        stockSimpleService.setStock(financialProductId, seckillActivityId, quantity);
        stockSimpleService.updateTokenBucket(financialProductId, seckillActivityId, quantity);
    }

    public void setCache(SaleProductDetail saleProductDetail) {
        setKey(saleProductDetail);
        redisUtils.set(saleProductDetailKey(saleProductDetail.getId(), saleProductDetail.getFinancialProductId(), saleProductDetail.getSeckillActivityId()), saleProductDetail);
    }

    public void deleteById(String id) {
        saleProductDetailMapper.deleteById(id);
        String key = getKey(id);
        if(key != null) {
            String[] orderFullKeyComponents = getSaleProductDetailKeyComponents(key);
            redisUtils.del(key);
            if(orderFullKeyComponents.length >= 3) {
                redisUtils.hdel(saleProductDetailActivityIdProductIdKeyMapKey(), orderFullKeyComponents[1] + "::" + orderFullKeyComponents[2]);
                redisUtils.hdel(saleProductDetailIdKeyMapKey(), orderFullKeyComponents[0]);
            }

        }
    }
}
