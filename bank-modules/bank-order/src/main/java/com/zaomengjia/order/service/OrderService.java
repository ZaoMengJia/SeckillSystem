package com.zaomengjia.order.service;

import com.zaomengjia.common.constant.OrderStatus;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.entity.SaleProductDetail;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.OrderVO;
import com.zaomengjia.common.vo.page.PageVO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 17:45
 */
public interface OrderService {
    OrderVO modelToVO(Order order);

    FinancialProductVO modelToVO(FinancialProduct financialProduct);

    PageVO<OrderVO> getUserOrderList(String userId);

    PageVO<OrderVO> getUserOrderList(String userId, int pageNum, int pageSize);

    Order getOrder(String orderId);

    String createOrder(String userId, String seckillActivityId, String financialProductId);

    SaleProductDetail getSaleProductDetailEntity(String saleProductDetailID);

    SaleProductDetail getSaleProductDetailEntity(String financialProductId, String seckillActivityId);

    FinancialProduct getFinancialProductEntity(String financialProductId);

    List<FinancialProduct> getFinancialProductEntityList(List<String> idList);

    OrderVO getOrderDetail(String id);

    OrderStatus getOrderStatus(String id);


}
