package com.zaomengjia.bankmanager.service;

import com.zaomengjia.common.vo.bank.SaleProductVO;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/16 00:05
 */
public interface SaleProductService {

    List<SaleProductVO> getList(String seckillActivityId);

    void insert(String financialProductId, String seckillActivityId, int quantity, int total);

    void delete(String seckillActivityId, String financialProductId);

    void modify(String seckillActivityId, String financialProductId, int quantity, int total);
}
