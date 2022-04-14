package service

import (
	"bank-seckill/common"
	"bank-seckill/model"
	"bank-seckill/repository"
)

type SeckillActivityService struct {
}

var dao = repository.SeckillActivityDao{}

func (*SeckillActivityService) FindSeckillActivityById(id string) *model.SeckillActivity {
	dao.DB = common.DB
	result, err := dao.GetSeckillActivityById(id)
	if err != nil {
		return nil
	}
	return result
}
