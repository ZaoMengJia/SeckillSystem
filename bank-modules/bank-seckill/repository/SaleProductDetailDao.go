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
	if err := s.DB.Debug().Table("sale_product_detail").Where("financial_product_id = ? and seckill_activity_id = ?", financialProductId, seckillActivityId).First(&result).Error; err != nil {
		return nil, err
	}

	return &result, nil
}
