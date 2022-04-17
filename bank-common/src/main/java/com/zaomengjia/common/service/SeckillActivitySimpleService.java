package com.zaomengjia.common.service;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.constant.RedisKey;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.utils.RedisUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/16 01:35
 */
@Service
public class SeckillActivitySimpleService {

    private final RedisUtils redisUtils;

    public SeckillActivitySimpleService(
            RedisUtils redisUtils
    ) {
        this.redisUtils = redisUtils;
    }

    public void setCache(SeckillActivity seckillActivity) {
        String key = RedisKey.seckillActivityKey(seckillActivity.getId());
        redisUtils.set(key, seckillActivity);
    }

    public SeckillActivity getCache(String id) {
        String key = RedisKey.seckillActivityKey(id);
        Object o = redisUtils.get(key);
        if(o != null) {
            return ((JSONObject)o).toJavaObject(SeckillActivity.class);
        }
        else {
            return null;
        }
    }

    public void setCache(List<SeckillActivity> list) {
        Map<String, Object> map = list.stream().collect(Collectors.toMap(c -> RedisKey.seckillActivityKey(c.getId()), c -> c));
        redisUtils.multiSet(map);
    }

    public void deleteCache(String id) {
        redisUtils.del(RedisKey.seckillActivityKey(id));
    }
}
