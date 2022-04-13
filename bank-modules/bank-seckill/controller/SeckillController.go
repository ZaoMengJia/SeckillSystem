package controller

import (
	"crypto/md5"
	"encoding/base64"
	"errors"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	jsoniter "github.com/json-iterator/go"
	"github.com/spf13/viper"
	"goweb/response"
	"goweb/service"
	"strings"
	"time"
)

var jwtKey = viper.GetString("jwt.key")
var orderService = service.OrderService{}
var seckillActivityService = service.SeckillActivityService{}

func GetOrderStatus(ctx *gin.Context) {
	orderId := ctx.Param("orderId")
	orderStatus := orderService.GetCacheOrder(orderId)
	response.SuccessData(ctx, gin.H{
		"status": orderStatus,
	})
}

func GetSeckillSecretUrl(ctx *gin.Context) {
	seckillActivityId := ctx.Param("seckillActivityId")
	token := ctx.GetHeader("Authorization")

	if seckillActivityId == "" || token == "" {
		response.Error(ctx, response.ResultCodePatternError)
		return
	}

	activity := seckillActivityService.FindSeckillActivityById(seckillActivityId)
	now := time.Now()
	if activity.BeginTime.After(now) && activity.EndTime.Before(now) {
		response.Error(ctx, response.ResultCodeActivityNotStarted)
		return
	}

	userId, err := getUserIdFromJwtPayload(token)
	if err != nil {
		response.Error(ctx, response.ResultCodePatternError)
		return
	}

	pathKey := viper.GetString("seckill.path-key")
	correctPathByte := md5.Sum([]byte(seckillActivityId + userId + pathKey))
	correctPath := fmt.Sprintf("%x", correctPathByte)

	response.SuccessData(ctx, gin.H{
		"path": "/" + correctPath,
	})
}

func Seckill(ctx *gin.Context) {
	//path := ctx.Param("path")
	seckillActivityId := ctx.Query("seckillActivityId")
	financialProductId := ctx.Query("financialProductId")
	token := ctx.GetHeader("Authorization")

	if seckillActivityId == "" || financialProductId == "" || token == "" {
		response.Error(ctx, response.ResultCodePatternError)
		return
	}

	//解密jwt
	//payloadBase64 := strings.Split(token, ".")[1]
	//payloadStr, _ := base64.StdEncoding.DecodeString(payloadBase64)
	//userId := jsoniter.Get(payloadStr, "userId").ToString()

	//判断path是不是正确的
	//pathKey := viper.GetString("seckill.path-key")
	//correctPathByte := md5.Sum([]byte(seckillActivityId + userId + pathKey))
	//correctPath := fmt.Sprintf("%x", correctPathByte)
	//if path != strings.ToTitle(correctPath) {
	//	//异常处理
	//	return
	//}

	orderId, err := orderService.CreateOrder(uuid.NewString(), seckillActivityId, financialProductId)
	if err == nil {
		response.SuccessData(ctx, gin.H{
			"orderId": orderId,
		})
	} else {
		var appException *response.AppException
		if errors.As(err, &appException) {
			response.ErrorException(ctx, *appException)
		} else {
			response.ErrorMessage(ctx, response.ResultCodeInternalServerError, err.Error())
		}
	}
}

func getUserIdFromJwtPayload(jwt string) (string, error) {
	payloadBase64 := strings.Split(jwt, ".")[1]
	payloadStr, err := base64.StdEncoding.DecodeString(payloadBase64)
	if err != nil {
		return "", err
	}
	userId := jsoniter.Get(payloadStr, "userId").ToString()
	return userId, nil
}
