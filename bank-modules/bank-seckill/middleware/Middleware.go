package middleware

import (
	"bytes"
	"crypto/md5"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/go-redis/redis"
	"github.com/golang-jwt/jwt"
	"github.com/spf13/viper"
	"goweb/common"
	"goweb/response"
	"io/ioutil"
	"log"
	"net/http"
	"net/url"
	"regexp"
	"sort"
	"strconv"
	"strings"
	"time"
)

func SignatureInterceptor() gin.HandlerFunc {
	return func(ctx *gin.Context) {
		request := ctx.Request
		header := request.Header
		method := request.Method

		nonce := header.Get("nonce")
		timestamp := header.Get("timestamp")
		signature := header.Get("signature")

		if nonce == "" || timestamp == "" || signature == "" {
			ctx.Abort()
			response.Error(ctx, response.ResultCodePatternError)
		}

		data, err := ctx.GetRawData()
		if err != nil {
			log.Fatal(err)
		}
		ctx.Request.Body = ioutil.NopCloser(bytes.NewBuffer(data))

		body := string(data)

		if method == http.MethodPost && strings.Contains(header.Get("Content-Type"), "json") {

			if !verifySignature(body, nonce, timestamp, signature) {
				ctx.Abort()
				response.Error(ctx, response.ResultCodeSignatureError)
			}

		} else {
			paramMap := make(map[string]string)

			//query
			queryRawMap := ctx.Request.URL.Query()
			for key, value := range queryRawMap {
				paramMap[key] = value[0]
			}

			//body
			bodyArray := strings.Split(body, "&")
			if len(bodyArray) > 0 {
				for _, param := range bodyArray {
					if param == "" {
						continue
					}

					split := strings.Split(param, "=")
					if len(split) != 2 {
						ctx.Abort()
						response.Error(ctx, response.ResultCodeSignatureError)
						return
					}

					value, err := url.QueryUnescape(split[1])
					if err != nil {
						ctx.Abort()
						response.Error(ctx, response.ResultCodeSignatureError)
						return
					}
					paramMap[split[0]] = value
				}
			}

			keys := make([]string, 0, len(paramMap))
			sortedParamMap := make(map[string]string)
			sort.Strings(keys)
			for _, key := range keys {
				sortedParamMap[key] = paramMap[key]
			}

			inputByte, _ := json.Marshal(sortedParamMap)
			input := string(inputByte)
			verifySignature(input, nonce, timestamp, signature)
		}

		ctx.Next()
	}
}

func verifySignature(input string, nonce string, timestampStr string, signature string) bool {
	//1. 时间戳
	timestamp, err := strconv.ParseInt(timestampStr, 10, 64)
	expireTime := viper.GetInt64("expired-time")
	if err != nil || time.Now().UnixMilli()-timestamp > expireTime {
		return false
	}

	//2. nonce有没有被用过
	_, err = common.RedisTemplate.Get("auth::nonce::" + nonce).Result()
	if err != redis.Nil {
		return false
	}

	//3. 验签
	appKey := viper.GetString("sign.app-key")
	correctMd5 := fmt.Sprintf("%x", md5.Sum([]byte(input+nonce+timestampStr+appKey)))
	correctSignature := base64.StdEncoding.EncodeToString([]byte(correctMd5))
	correctSignature = regexp.MustCompile("[=+/]").ReplaceAllString(correctSignature, "")

	if signature != correctSignature {
		return false
	}

	//4. nonce作废
	common.RedisTemplate.Set("auth::nonce::"+nonce, nonce, 1*time.Hour)
	return true
}

func AuthInterceptor() gin.HandlerFunc {
	return func(ctx *gin.Context) {
		request := ctx.Request
		token := request.Header.Get("Authorization")

		if token == "" {
			response.Error(ctx, response.ResultCodePatternError)
			ctx.Abort()
			return
		}

		//1. 检查token
		strings.ReplaceAll("Bearer ", token, token)
		jwtSecret := viper.GetString("jwt.key")
		parse, err := jwt.Parse(token, func(token *jwt.Token) (interface{}, error) {
			return jwtSecret, nil
		})
		if err != nil {
			response.Error(ctx, response.ResultCodePatternError)
			ctx.Abort()
			return
		}

		//2. 判断过期了没有
		claims := parse.Claims.(jwt.MapClaims)
		jwtExpiredTime := viper.GetInt64("jwt.expired-time")
		if time.Now().UnixMicro()-claims["timestamp"].(int64) > jwtExpiredTime*1000 {
			response.Error(ctx, response.ResultCodeTokenExpired)
			ctx.Abort()
			return
		}

		userId := claims["userId"].(string)
		//3. 判断是不是恶意访问
		//根据访问次数判断
		requestKey := "user::request-time::" + userId
		requestTime, err := common.RedisTemplate.Get(requestKey).Int()
		if err != redis.Nil && requestTime > 400 {
			response.Error(ctx, response.ResultCodeHaveARestPlease)
			ctx.Abort()
			return
		}

		//4. 授权
		common.RedisTemplate.Incr(requestKey)
		common.RedisTemplate.Expire(requestKey, 30*time.Minute)

		request.Header.Set("userId", claims["userId"].(string))
	}
}
