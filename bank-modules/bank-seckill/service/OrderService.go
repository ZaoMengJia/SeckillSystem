package service

import (
	"bank-seckill/common"
	"bank-seckill/model"
	"bank-seckill/response"
	"encoding/json"
	"fmt"
	"github.com/go-redis/redis"
	"github.com/google/uuid"
	"github.com/wagslane/go-rabbitmq"
	"strings"
	"time"
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
	redisJson, err := common.RedisTemplate.Get(result).Result()

	var order model.Order
	err = json.Unmarshal([]byte(redisJson), &order)
	if err != nil {
		panic(err)
		return nil
	}

	return &order.Status
}

func (r *OrderService) CreateOrder(userId string, seckillActivityId string, financialProductId string) (string, error) {
	//判读活动存不存在
	result := detailService.FindSaleProductByFinancialProductIdAndSeckillActivityId(financialProductId, seckillActivityId)
	if result == nil {
		return "", &response.AppException{ExceptionType: response.ResultCodeNoSuchActivity}
	}

	//判断这个用户下订单了吗
	userOrderQuantityTotalKey := fmt.Sprintf("order-user-id-financial-product-id-seckill-activity-id-map::%s::%s::%s", userId, result.FinancialProductId, result.SeckillActivityId)
	userOrderQuantityTotal, err := common.RedisTemplate.Get(userOrderQuantityTotalKey).Int()
	if err != nil {
		if err == redis.Nil {
			userOrderQuantityTotal = 0
		} else {
			return "", &response.AppException{ExceptionType: response.ResultCodeInternalServerError}
		}
	}
	if userOrderQuantityTotal >= 1 {
		return "", &response.AppException{ExceptionType: response.ResultCodeOrderExceedLimit}
	}

	//组建订单
	var order = model.Order{
		SeckillActivityId:  seckillActivityId,
		FinancialProductId: financialProductId,
		UserId:             userId,
		Quantity:           1,
	}

	//获取令牌，如果可以购买多份，这里也要做更改
	isSuccess := service.AttemptGetToken(financialProductId, seckillActivityId)
	if !isSuccess {
		//卖完了
		return "", &response.AppException{ExceptionType: response.ResultCodeSellOut}
	}

	id := strings.ReplaceAll(uuid.New().String(), "-", "")
	order.Id = id
	order.Status = "CREATING"

	key := fmt.Sprintf("order::%s::%s::%s::%s", order.Id, order.FinancialProductId, order.SeckillActivityId, order.UserId)
	orderJson, _ := json.Marshal(order)
	common.RedisTemplate.HSet("order-id-key-map", order.Id, key)
	common.RedisTemplate.IncrBy(userOrderQuantityTotalKey, int64(order.Quantity))
	common.RedisTemplate.Set(key, string(orderJson), 24*time.Hour)

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
