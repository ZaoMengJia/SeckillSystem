package com.zaomengjia.stock.service.impl;

import com.zaomengjia.common.dao.SaleProductDetailMapper;
import com.zaomengjia.common.service.StockSimpleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/12 12:06
 */
@Service
@EnableScheduling
public class StockPersistService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StockSimpleService stockSimpleService;

    private final SaleProductDetailMapper saleProductDetailMapper;

    public StockPersistService(StockSimpleService stockSimpleService, SaleProductDetailMapper saleProductDetailMapper) {
        this.stockSimpleService = stockSimpleService;
        this.saleProductDetailMapper = saleProductDetailMapper;
    }

//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0/15 * * * * ?")
    public void updateStock() {
        logger.info("开始持久化更新库存");
        Set<String> keySet = stockSimpleService.getDirtyStockKeySet();
        keySet.forEach(rawKey -> {
            String[] split = rawKey.split("::");
            String financialProductId = split[0];
            String seckillActivityId = split[1];
            long stock = stockSimpleService.getStock(financialProductId, seckillActivityId);
            int i = saleProductDetailMapper.updateQuantity(financialProductId, seckillActivityId, stock);
            if(i > 0) {
                stockSimpleService.setDirtyStock(financialProductId, seckillActivityId, false);
            }
            else {
                logger.error("持久化更新库存失败 要更新{} => {}", rawKey, stock);
            }
        });
    }
}
