package com.zaomengjia.service;


import com.zaomengjia.pojo.SeckillActivity;

import java.util.Map;

public interface SeckillActivityService {
    Boolean seckillActivityExist(String activityName);

    Map<String ,Object> getSeckillActivity(int pageIndex, int pageSize);

    SeckillActivity getSeckillActivityByName(String name);

    SeckillActivity getSeckillActivityById(long id);

    int addSeckillActivity(SeckillActivity seckillActivity);

    int deleteSeckillActivity(long id);

    int updateSeckillActivity(SeckillActivity seckillActivity);


}
