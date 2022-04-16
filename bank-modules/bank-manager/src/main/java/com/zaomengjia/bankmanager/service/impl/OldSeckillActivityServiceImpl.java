//package com.zaomengjia.bankmanager.service.impl;
//
//import com.zaomengjia.common.dao.SeckillActivityMapper;
//import com.zaomengjia.common.entity.SeckillActivity;
//import com.zaomengjia.bankmanager.service.SeckillActivityService;
//import com.zaomengjia.common.vo.page.PageVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//@Service
//public class OldSeckillActivityServiceImpl implements SeckillActivityService {
//
//    @Autowired
//    SeckillActivityMapper seckillActivityMapper;
//
//    @Override
//    public Boolean seckillActivityExist(String activityName) {
//        return seckillActivityMapper.findByName(activityName) != null;
//    }
//
//    @Override
//    public PageVO<SeckillActivity> getSeckillActivity(int pageIndex, int pageSize) {
//        Page<SeckillActivity> page = seckillActivityMapper.findAll(PageRequest.of(pageIndex - 1, pageSize));
//        return new PageVO<>(page);
//    }
//
//    @Override
//    public PageVO<SeckillActivity> searchSeckillActivity(String keyword, int pageIndex, int pageSize) {
//        Page<SeckillActivity> result = seckillActivityMapper.findByNameContaining(keyword, PageRequest.of(pageIndex - 1, pageSize));
//        return new PageVO<>(result);
//    }
//
//    @Override
//    public SeckillActivity getSeckillActivityByName(String name) {
//        return seckillActivityMapper.findByName(name);
//    }
//
//    @Override
//    public SeckillActivity getSeckillActivityById(String id) {
//        return seckillActivityMapper.findById(id).orElse(null);
//    }
//
//    @Override
//    public void addSeckillActivity(SeckillActivity seckillActivity) {
//        seckillActivityMapper.save(seckillActivity);
//    }
//
//    @Override
//    public void deleteSeckillActivity(String id) {
//        seckillActivityMapper.deleteById(id);
//    }
//
//    @Override
//    public void updateSeckillActivity(SeckillActivity seckillActivity) {
//        seckillActivityMapper.save(seckillActivity);
//    }
//}
