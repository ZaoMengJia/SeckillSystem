package common

import (
	"github.com/go-redis/redis"
	"strconv"
)

var RedisTemplate *redis.Client

func InitRedis(url string, port int, password string) (err error) {
	RedisTemplate = redis.NewClient(&redis.Options{
		Addr:     url + ":" + strconv.Itoa(port),
		Password: password,
		DB:       0,
	})

	_, err = RedisTemplate.Ping().Result()
	if err != nil {
		return err
	}
	return nil
}
