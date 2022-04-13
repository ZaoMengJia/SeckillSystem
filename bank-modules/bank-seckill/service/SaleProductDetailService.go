package service

import (
	"bank-seckill/common"
	"bank-seckill/model"
	"bank-seckill/repository"
	"encoding/json"
	"fmt"
	"github.com/go-redis/redis"
)

type SaleProductDetailService struct {
}

func (*SaleProductDetailService) GetSaleProductDetailKey(productId string, activityId string) (key string, err error) {
	key, err = common.RedisTemplate.HGet("sale-product-detail-activity-id-product-id-key-map", activityId+"::"+productId).Result()
	switch {
	case err == redis.Nil:
		return "", nil
	case err != nil:
		return "", err
	}
	return key, nil
}

func (s *SaleProductDetailService) FindSaleProductByFinancialProductIdAndSeckillActivityId(productId string, activityId string) *model.SaleProductDetail {
	key, err := s.GetSaleProductDetailKey(productId, activityId)
	if key == "" || err != nil {
		//从数据库拿
		dao := repository.SaleProductDetailDao{DB: common.DB}
		saleProductDetail, err := dao.FindByFinancialProductIdAndSeckillActivityId(productId, activityId)
		if err != nil || saleProductDetail == nil {
			return nil
		}

		//存入redis
		redisJson, err := json.Marshal(saleProductDetail)
		if err != nil {
			return saleProductDetail
		}
		key := fmt.Sprintf("sale-product-detail::%s::%s::%s", saleProductDetail.Id, saleProductDetail.FinancialProductId, saleProductDetail.SeckillActivityId)
		common.RedisTemplate.Set(key, redisJson, 0)
		common.RedisTemplate.HSet("sale-product-detail-activity-id-product-id-key-map", saleProductDetail.SeckillActivityId+"::"+saleProductDetail.FinancialProductId, key)
		return saleProductDetail
	} else {
		var result = model.SaleProductDetail{}
		resultJson, err := common.RedisTemplate.Get(key).Result()
		if err != nil {
			panic(err)
			return nil
		}
		_ = json.Unmarshal([]byte(resultJson), &result)
		return &result
	}
}
