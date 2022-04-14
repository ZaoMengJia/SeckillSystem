package routes

import (
	"bank-seckill/controller"
	"bank-seckill/response"
	"github.com/gin-gonic/gin"
	"log"
	"runtime/debug"
)

func CollectRoute(r *gin.Engine) *gin.Engine {
	//签名拦截和授权拦截
	//r.Use(middleware.SignatureInterceptor(), middleware.AuthInterceptor())
	r.Use(func(ctx *gin.Context) {
		defer func() {
			if r := recover(); r != nil {
				log.Printf("panic: %v\n", r)
				debug.PrintStack()
				response.Error(ctx, response.ResultCodeInternalServerError)
			}
		}()
		ctx.Next()
	})
	{
		r.POST("/weixin/seckill/:path", controller.Seckill)
		r.GET("/weixin/seckill/url/:seckillActivityId", controller.GetSeckillSecretUrl)
		r.GET("/weixin/seckill/order/status/:orderId", controller.GetOrderStatus)
		r.NoRoute(func(ctx *gin.Context) {
			response.Error(ctx, response.ResultCodeNotFound)
		})
	}
	return r
}
