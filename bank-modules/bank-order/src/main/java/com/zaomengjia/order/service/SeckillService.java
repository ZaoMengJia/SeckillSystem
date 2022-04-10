package com.zaomengjia.order.service;

import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.vo.bank.SeckillActivityDetailVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import com.zaomengjia.common.vo.page.PageVO;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 18:18
 */
public interface SeckillService {
    SeckillActivityVO modelToVO(SeckillActivity seckillActivity);

    SeckillActivityVO getSeckillActivityById(String id);

    SeckillActivityDetailVO getSeckillActivityDetail(String id);

    SeckillActivity getSeckillActivityEntity(String id);

    PageVO<SeckillActivityVO> getSeckillActivityList(int pageNum, int pageSize);
}
