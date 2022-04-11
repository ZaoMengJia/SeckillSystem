package com.zaomengjia.common.service;

import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.utils.RedisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.zaomengjia.common.constant.RedisKey.*;

/**
 * dao+缓存的服务
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 00:02
 */
@Service
@Transactional
public class SaleProductDetailSimpleService {

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final RedisUtils redisUtils;

    public SaleProductDetailSimpleService(SaleProductDetailMapper saleProductDetailMapper, RedisUtils redisUtils) {
        this.saleProductDetailMapper = saleProductDetailMapper;
        this.redisUtils = redisUtils;
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
            redisUtils.hset(saleProductDetailMapKey(), saleProductDetailKey(detail.getId(), detail.getFinancialProductId(), detail.getSeckillActivityId()), detail);
            setKey(detail);
        }
        else {
            detail = (SaleProductDetail) redisUtils.hget(saleProductDetailMapKey(), key);
        }
        return detail;
    }

    public SaleProductDetail findByFinancialProductIdAndSeckillActivityId(String financialProductId, String seckillActivityId) {
        String key = getKey(seckillActivityId, financialProductId);
        SaleProductDetail detail;
        if(key == null) {
            detail = saleProductDetailMapper.findByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId);
            if(detail == null) {
                return null;
            }

            redisUtils.hset(saleProductDetailMapKey(), saleProductDetailKey(detail.getId(), detail.getFinancialProductId(), detail.getSeckillActivityId()), detail);
            setKey(detail);
        }
        else {
            detail = (SaleProductDetail) redisUtils.hget(saleProductDetailMapKey(), key);
        }
        return detail;
    }

    public void save(SaleProductDetail detail) {
        detail = saleProductDetailMapper.save(detail);
        redisUtils.hset(saleProductDetailMapKey(), saleProductDetailKey(detail.getId(), detail.getFinancialProductId(), detail.getSeckillActivityId()), detail);
        setKey(detail);
    }

    public void deleteById(String id) {
        saleProductDetailMapper.deleteById(id);
        String key = getKey(id);
        if(key != null) {
            String[] orderFullKeyComponents = getSaleProductDetailKeyComponents(key);
            redisUtils.hdel(saleProductDetailMapKey(), key);
            if(orderFullKeyComponents.length >= 3) {
                redisUtils.hdel(saleProductDetailActivityIdProductIdKeyMapKey(), orderFullKeyComponents[1] + "::" + orderFullKeyComponents[2]);
                redisUtils.hdel(saleProductDetailIdKeyMapKey(), orderFullKeyComponents[0]);
            }

        }
    }
}
