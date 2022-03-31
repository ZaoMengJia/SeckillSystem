package com.zaomengjia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaomengjia.dao.SeckillActivityMapper;
import com.zaomengjia.pojo.SeckillActivity;
import com.zaomengjia.service.SeckillActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SeckillAcitivityServiceImpl implements SeckillActivityService {

    @Autowired
    SeckillActivityMapper seckillActivityMapper;

    @Override
    public Boolean seckillActivityExist(String activityName) {
        QueryWrapper<SeckillActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sname",activityName);
        return seckillActivityMapper.selectOne(queryWrapper)!=null;
    }

    @Override
    public Map<String, Object> getSeckillActivity(int pageIndex, int pageSize) {
        Page<SeckillActivity> page = new Page<>(pageIndex, pageSize);
        Map<String, Object> map = new HashMap<>(5);
        seckillActivityMapper.selectPage(page, null);
        map.put("records", page.getRecords());
        map.put("total", page.getTotal());
        return map;
    }

    @Override
    public SeckillActivity getSeckillActivityByName(String name) {
        QueryWrapper<SeckillActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sname",name);
        return seckillActivityMapper.selectOne(queryWrapper);
    }

    @Override
    public SeckillActivity getSeckillActivityById(long id) {
        return seckillActivityMapper.selectById(id);
    }

    @Override
    public int addSeckillActivity(SeckillActivity seckillActivity) {
        return seckillActivityMapper.insert(seckillActivity);
    }

    @Override
    public int deleteSeckillActivity(long id) {
        return seckillActivityMapper.deleteById(id);
    }

    @Override
    public int updateSeckillActivity(SeckillActivity seckillActivity) {
        return seckillActivityMapper.updateById(seckillActivity);
    }
}
