package response

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

type ResultCode struct {
	code    int
	message string
}

type AppException struct {
	ExceptionType ResultCode
}

func (*AppException) Error() string {
	return ""
}

var (
	ResultCodeSuccess = ResultCode{
		code:    10000,
		message: "成功",
	}
	ResultCodeNotFound = ResultCode{
		code:    20001,
		message: "迷路啦，您是想去华师吗",
	}
	ResultCodeInternalServerError = ResultCode{
		code:    20002,
		message: "服务器抛给你一个错误",
	}
	ResultCodePatternError = ResultCode{
		code:    20003,
		message: "访问接口都不带全部参数都，想要干嘛？",
	}

	ResultCodeSignatureError = ResultCode{
		code:    20005,
		message: "签名错啦大哥，好好回去看代码吧",
	}
	ResultCodeHaveARestPlease = ResultCode{
		code:    20006,
		message: "大哥，喝瓶可乐冷静一下，那么急干嘛",
	}
	ResultCodeSellOut = ResultCode{
		code:    30010,
		message: "已售罄",
	}
	ResultCodeNoSuchActivity = ResultCode{
		code:    30005,
		message: "没有这个秒杀活动",
	}
	ResultCodeOrderExceedLimit = ResultCode{
		code:    30012,
		message: "每人限购哦～不能贪多哦～",
	}
	ResultCodeActivityNotStarted = ResultCode{
		code:    30013,
		message: "活动未开始",
	}

	ResultCodeTokenExpired = ResultCode{
		code:    40002,
		message: "登录信息超时，请重新登录",
	}
	ResultCodeTokenError = ResultCode{
		code:    40003,
		message: "请重新登录",
	}
)

func ResponseFull(ctx *gin.Context, code int, data gin.H, message string) {
	ctx.JSON(http.StatusOK, gin.H{
		"code":    code,
		"data":    data,
		"message": message,
	})
}

func ResponseMessage(ctx *gin.Context, code int, message string) {
	ctx.JSON(http.StatusOK, gin.H{
		"code":    code,
		"data":    nil,
		"message": message,
	})
}

func ResponseData(ctx *gin.Context, code int, data gin.H) {
	ctx.JSON(http.StatusOK, gin.H{
		"code":    code,
		"data":    data,
		"message": nil,
	})
}

func SuccessData(ctx *gin.Context, data gin.H) {
	ResponseFull(ctx, ResultCodeSuccess.code, data, ResultCodeSuccess.message)
}

func Success(ctx *gin.Context) {
	ResponseMessage(ctx, ResultCodeSuccess.code, ResultCodeSuccess.message)
}

func Error(ctx *gin.Context, resultCode ResultCode) {
	ResponseMessage(ctx, resultCode.code, resultCode.message)
}

func ErrorMessage(ctx *gin.Context, resultCode ResultCode, message string) {
	ResponseMessage(ctx, resultCode.code, message)
}

func ErrorException(ctx *gin.Context, exception AppException) {
	Error(ctx, exception.ExceptionType)
}
