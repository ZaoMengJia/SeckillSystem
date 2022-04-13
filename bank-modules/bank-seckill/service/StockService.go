package service

import "goweb/common"

type StockService struct {
}

func (*StockService) AttemptGetToken(financialProductId string, seckillActivityId string) bool {
	_, err := common.RedisTemplate.SPop("seckill-token-bucket::" + financialProductId + "::" + seckillActivityId).Result()
	if err != nil {
		return false
	}
	return true
}
