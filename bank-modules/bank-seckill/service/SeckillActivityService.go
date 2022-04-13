package service

import (
	"goweb/common"
	"goweb/model"
	"goweb/repository"
)

type SeckillActivityService struct {
}

var dao = repository.SeckillActivityDao{DB: common.DB}

func (*SeckillActivityService) FindSeckillActivityById(id string) *model.SeckillActivity {
	result, err := dao.GetSeckillActivityById(id)
	if err != nil {
		return nil
	}
	return result
}
