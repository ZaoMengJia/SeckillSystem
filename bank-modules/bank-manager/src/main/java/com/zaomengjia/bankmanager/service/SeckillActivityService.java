package com.zaomengjia.bankmanager.service;


import com.zaomengjia.bankmanager.dto.SeckillActivityDto;
import com.zaomengjia.bankmanager.vo.SeckillActivityWithProductListVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import com.zaomengjia.common.vo.page.PageVO;

public interface SeckillActivityService {

    PageVO<SeckillActivityVO> getList(int pageNum, int pageSize);

    PageVO<SeckillActivityWithProductListVO> getListWithDetail(int pageNum, int pageSize);

    PageVO<SeckillActivityWithProductListVO> searchByName(String name, int pageNum, int pageSize);

    String create(SeckillActivityDto dto);

    void modify(String id, SeckillActivityDto dto);

    void delete(String id);

    SeckillActivityWithProductListVO getDetail(String id);
}
