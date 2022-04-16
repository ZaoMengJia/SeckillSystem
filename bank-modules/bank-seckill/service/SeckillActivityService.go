package service

import (
	"bank-seckill/common"
	"bank-seckill/model"
	"bank-seckill/repository"
	"encoding/json"
	"github.com/go-redis/redis"
)

type SeckillActivityService struct {
}

var dao = repository.SeckillActivityDao{}

func (*SeckillActivityService) FindSeckillActivityById(id string) *model.SeckillActivity {
	jsonStr, err := common.RedisTemplate.Get("seckill-activity::" + id).Result()
	if err != nil {
		if err == redis.Nil {
			dao.DB = common.DB
			result, err := dao.GetSeckillActivityById(id)
			if err != nil {
				return nil
			}
			return result
		} else {
			return nil
		}
	} else {
		var activity model.SeckillActivity
		err := json.Unmarshal([]byte(jsonStr), &activity)
		if err != nil {
			return nil
		}

		return &activity
	}
}
