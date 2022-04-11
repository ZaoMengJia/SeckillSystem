package com.zaomengjia.common.service;

import cn.hutool.core.lang.UUID;
import com.zaomengjia.common.utils.RedisUtils;
import org.springframework.stereotype.Service;

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

    public StockSimpleService(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public void addTokenBucket(String financialProductId, String seckillActivityId, long total) {

        Set<String> tokenSet = LongStream.rangeClosed(1, total).mapToObj(c -> UUID.fastUUID().toString(true)).collect(Collectors.toSet());
        if(tokenSet.size() != total) {
            tokenSet = LongStream.rangeClosed(1, total).mapToObj(c -> UUID.fastUUID().toString(true)).collect(Collectors.toSet());
        }
        redisUtils.sSet(tokenBucketMapKey(financialProductId, seckillActivityId), tokenSet);
    }

    public void deleteTokenBucket(String financialProductId, String seckillActivityId) {
        redisUtils.del(tokenBucketMapKey(financialProductId, seckillActivityId));
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

    public boolean consumeToken(String financialProductId, String seckillActivityId, String token) {
        long l = redisUtils.setRemove(tokenBucketMapKey(financialProductId, seckillActivityId), token);
        return l > 0;
    }
}
