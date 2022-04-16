package com.zaomengjia.bankmanager.service;

import com.zaomengjia.bankmanager.vo.OrderVO;
import com.zaomengjia.common.vo.page.PageVO;

public interface OrderService {

    PageVO<OrderVO> getOrderList(int pageNum, int pageSize);

    PageVO<OrderVO> searchOrderById(String keyword, int pageNum, int pageSize);

}
