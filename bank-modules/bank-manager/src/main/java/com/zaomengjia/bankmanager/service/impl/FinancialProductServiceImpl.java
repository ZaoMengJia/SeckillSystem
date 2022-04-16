package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.bankmanager.dto.FinancialProductDto;
import com.zaomengjia.bankmanager.service.FinancialProductService;
import com.zaomengjia.common.dao.FinancialProductMapper;
import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.entity.FinancialProduct;
import com.zaomengjia.common.utils.ModelUtils;
import com.zaomengjia.common.vo.bank.FinancialProductVO;
import com.zaomengjia.common.vo.page.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 23:29
 */
@Service
public class FinancialProductServiceImpl implements FinancialProductService {

    private final FinancialProductMapper financialProductMapper;

    private final SaleProductDetailMapper saleProductDetailMapper;

    private final ModelUtils modelUtils;

    public FinancialProductServiceImpl(
            FinancialProductMapper financialProductMapper,
            ModelUtils modelUtils,
            SaleProductDetailMapper saleProductDetailMapper
    ) {
        this.financialProductMapper = financialProductMapper;
        this.modelUtils = modelUtils;
        this.saleProductDetailMapper = saleProductDetailMapper;
    }

    @Override
    public PageVO<FinancialProductVO> getList(int pageNum, int pageSize) {
        Page<FinancialProduct> page = financialProductMapper.findAll(PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(page.map(modelUtils::toFinancialProductVO));
    }

    @Override
    public PageVO<FinancialProductVO> searchByName(String name, int pageNum, int pageSize) {
        Page<FinancialProduct> page = financialProductMapper.findByNameContaining(name, PageRequest.of(pageNum - 1, pageSize));
        return new PageVO<>(page.map(modelUtils::toFinancialProductVO));
    }

    @Override
    public String create(FinancialProductDto dto) {
        FinancialProduct entity = new FinancialProduct();
        entity.setName(dto.getName());
        entity.setPrice((int) (dto.getPrice() * 100));

        entity = financialProductMapper.save(entity);
        return entity.getId();
    }

    @Override
    public void modify(String id, FinancialProductDto dto) {
        FinancialProduct entity = financialProductMapper.findById(id).orElse(null);
        if(entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setPrice((int) (dto.getPrice() * 100));
        financialProductMapper.save(entity);
    }

    @Override
    @Transactional
    public void delete(String id) {
        financialProductMapper.deleteById(id);
        saleProductDetailMapper.deleteByFinancialProductId(id);
    }


    @Override
    public FinancialProductVO getProductById(String pid) {
        FinancialProduct financialProduct = financialProductMapper.findById(pid).orElse(null);
        if(financialProduct == null){
            return null;
        }
        FinancialProductVO vo = new FinancialProductVO();
        BeanUtils.copyProperties(financialProduct, vo);
        return vo;
    }
}
