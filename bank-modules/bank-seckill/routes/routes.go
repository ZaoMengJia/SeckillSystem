package routes

import (
	"bank-seckill/controller"
	"github.com/gin-gonic/gin"
)

func CollectRoute(r *gin.Engine) *gin.Engine {
	//签名拦截和授权拦截
	//r.Use(middleware.SignatureInterceptor(), middleware.AuthInterceptor())
	{
		r.POST("/weixin/seckill/:path", controller.Seckill)
		r.GET("/weixin/seckill/url/:seckillActivityId", controller.GetSeckillSecretUrl)
		r.GET("/weixin/seckill/order/status/:orderId", controller.GetOrderStatus)
	}
	return r
}
