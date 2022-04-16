package com.zaomengjia.bankmanager.service;

import com.zaomengjia.bankmanager.dto.FinancialProductDto;
import com.zaomengjia.bankmanager.dto.SeckillActivityDto;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import com.zaomengjia.common.vo.page.PageVO;

import java.util.Map;

public interface FinancialProductService {
    PageVO<FinancialProductVO> getList(int pageNum, int pageSize);

    PageVO<FinancialProductVO> searchByName(String name, int pageNum, int pageSize);

    String create(FinancialProductDto dto);

    void modify(String id, FinancialProductDto dto);

    void delete(String id);
}
