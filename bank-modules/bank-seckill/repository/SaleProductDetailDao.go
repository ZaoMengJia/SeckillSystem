package repository

import (
	"bank-seckill/model"
	"gorm.io/gorm"
)

type SaleProductDetailDao struct {
	DB *gorm.DB
}

func (s *SaleProductDetailDao) FindByFinancialProductIdAndSeckillActivityId(financialProductId string, seckillActivityId string) (*model.SaleProductDetail, error) {
	var result model.SaleProductDetail
	if err := s.DB.Debug().Table("sale_product_detail").Where(&model.SaleProductDetail{
		FinancialProductId: financialProductId,
		SeckillActivityId:  seckillActivityId,
	}).First(&result).Error; err != nil {
		return nil, err
	}
	if result.Id == "" {
		return nil, nil
	}
	return &result, nil
}
