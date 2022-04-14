package repository

import (
	"bank-seckill/model"
	"gorm.io/gorm"
)

type SeckillActivityDao struct {
	DB *gorm.DB
}

func (s *SeckillActivityDao) GetSeckillActivityById(id string) (*model.SeckillActivity, error) {
	var result model.SeckillActivity
	if err := s.DB.Debug().Table("seckill_activity").
		Where(&model.SeckillActivity{Id: id}).Find(&result).Error; err != nil {
		panic(err)
		return nil, err
	}

	if result.Id == "" {
		return nil, nil
	}
	return &result, nil
}
