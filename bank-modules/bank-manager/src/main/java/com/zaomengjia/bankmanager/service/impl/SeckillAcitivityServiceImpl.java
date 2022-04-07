package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.common.dao.SeckillActivityMapper;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.bankmanager.service.SeckillActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SeckillAcitivityServiceImpl implements SeckillActivityService {

    @Autowired
    SeckillActivityMapper seckillActivityMapper;

    @Override
    public Boolean seckillActivityExist(String activityName) {
        return seckillActivityMapper.getBySname(activityName) != null;
    }

    @Override
    public Map<String, Object> getSeckillActivity(int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        Page<SeckillActivity> page = seckillActivityMapper.findAll(PageRequest.of(pageIndex, pageSize));
        map.put("records", page.toList());
        map.put("total", page.getTotalElements());
        return map;
    }

    @Override
    public Map<String, Object> searchSeckillActivity(String keyword, int pageIndex, int pageSize) {
        HashMap<String, Object> map = new HashMap<>();
        Page<SeckillActivity> result = seckillActivityMapper.getBySnameLike(keyword, PageRequest.of(pageIndex, pageSize));
        map.put("records", result.toList());
        map.put("total", result.getTotalElements());
        return map;
    }

    @Override
    public SeckillActivity getSeckillActivityByName(String name) {
        return seckillActivityMapper.getBySname(name);
    }

    @Override
    public SeckillActivity getSeckillActivityById(long id) {
        return seckillActivityMapper.findById(id).orElse(null);
    }

    @Override
    public void addSeckillActivity(SeckillActivity seckillActivity) {
        seckillActivityMapper.save(seckillActivity);
    }

    @Override
    public void deleteSeckillActivity(long id) {
        seckillActivityMapper.deleteById(id);
    }

    @Override
    public void updateSeckillActivity(SeckillActivity seckillActivity) {
        seckillActivityMapper.save(seckillActivity);
    }
}
