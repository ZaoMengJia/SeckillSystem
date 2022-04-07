package com.zaomengjia.bankmanager.service;


import com.zaomengjia.common.entity.SeckillActivity;

import java.util.Map;

public interface SeckillActivityService {
    Boolean seckillActivityExist(String activityName);

    Map<String ,Object> getSeckillActivity(int pageIndex, int pageSize);

    Map<String, Object> searchSeckillActivity(String keyword, int pageIndex, int pageSize);

    SeckillActivity getSeckillActivityByName(String name);

    SeckillActivity getSeckillActivityById(long id);

    void addSeckillActivity(SeckillActivity seckillActivity);

    void deleteSeckillActivity(long id);

    void updateSeckillActivity(SeckillActivity seckillActivity);


}
