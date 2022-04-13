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
	if err := s.DB.Find(&result, id).Error; err != nil {
		return nil, err
	}
	return &result, nil
}
