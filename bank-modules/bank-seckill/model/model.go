package model

import "time"

type SaleProductDetail struct {
	Id                 string `gorm:"primaryKey"`
	SeckillActivityId  string
	FinancialProductId string
	Quantity           int64
	Total              int
	Version            int
}

type Order struct {
	Id                 string     `json:"id"`
	UserId             string     `json:"userId"`
	FinancialProductId string     `json:"financialProductId"`
	SeckillActivityId  string     `json:"seckillActivityId"`
	Quantity           string     `json:"quantity"`
	Status             string     `json:"status"`
	CreateTime         *time.Time `json:"createTime"`
}

type SeckillActivity struct {
	Id         string `gorm:"primaryKey"`
	Name       string
	Image      string
	Detail     string
	BeginTime  *time.Time `gorm:"column:activity_begin_time"`
	EndTime    *time.Time `gorm:"column:activity_end_time"`
	CreateTime *time.Time
	Deleted    int
}
