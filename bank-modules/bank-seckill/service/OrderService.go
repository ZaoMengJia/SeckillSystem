package service

import (
	"bank-seckill/common"
	"bank-seckill/model"
	"bank-seckill/response"
	"encoding/json"
	"github.com/go-redis/redis"
	"github.com/google/uuid"
	"github.com/wagslane/go-rabbitmq"
	"strings"
)

type OrderService struct {
}

var detailService = SaleProductDetailService{}
var service = StockService{}

func (*OrderService) GetCacheOrder(orderId string) *string {
	//直接从redis拿
	result, err := common.RedisTemplate.HGet("order-id-key-map", orderId).Result()
	if err == redis.Nil {
		return nil
	}
	result = result[1 : len(result)-1]
	redisJson, err := common.RedisTemplate.Get(result).Result()

	var order = model.Order{}
	err = json.Unmarshal([]byte(redisJson), &order)
	if err != nil {
		return nil
	}

	return &order.Status
}

func (r *OrderService) CreateOrder(userId string, seckillActivityId string, financialProductId string) (string, error) {
	result := detailService.FindSaleProductByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId)

	if result == nil {
		return "", &response.AppException{ExceptionType: response.ResultCodeNoSuchActivity}
	}

	var order = model.Order{
		SeckillActivityId:  seckillActivityId,
		FinancialProductId: financialProductId,
		UserId:             userId,
	}

	//获取令牌
	isSuccess := service.AttemptGetToken(financialProductId, seckillActivityId)
	if !isSuccess {
		//卖完了
		return "", &response.AppException{ExceptionType: response.ResultCodeSellOut}
	}

	id := uuid.New().String()
	strings.ReplaceAll("-", id, id)
	order.Id = id
	order.Status = "CREATING"

	//存队列
	go pushMessageQueue(order)

	return order.Id, nil
}

func pushMessageQueue(order model.Order) {

	orderJson, _ := json.Marshal(&order)
	err := common.MQPublisher.Publish(
		orderJson,
		[]string{"Create"},
		rabbitmq.WithPublishOptionsContentType("application/json"),
		rabbitmq.WithPublishOptionsMandatory,
		rabbitmq.WithPublishOptionsPersistentDelivery,
		rabbitmq.WithPublishOptionsExchange("DirectExchange"),
	)
	if err != nil {
		panic(err)
	}
}
