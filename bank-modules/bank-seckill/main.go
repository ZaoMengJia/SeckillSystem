package main

import (
	"github.com/gin-gonic/gin"
	"github.com/spf13/viper"
	"goweb/common"
	"os"
	"strconv"
)

func main() {
	initConfig()
	initRedis()
	initDatabase()
	common.InitRabbitMQ()
	common.NacosInit()
	r := gin.Default()
	r = CollectRoute(r)

	port := viper.GetInt("server.port")
	defer common.MQPublisher.Close()
	panic(r.Run(":" + strconv.Itoa(port)))

}

func initDatabase() {
	username := viper.GetString("datasource.username")
	password := viper.GetString("datasource.password")
	url := viper.GetString("datasource.url")
	database := viper.GetString("datasource.database")
	common.InitDatabase(username, password, url, database)
}

func initRedis() {
	password := viper.GetString("redis.password")
	url := viper.GetString("redis.url")
	port := viper.GetInt("redis.port")
	common.InitRedis(url, port, password)
}

func initConfig() {
	workDir, _ := os.Getwd()
	viper.SetConfigName("application")
	viper.SetConfigType("yml")
	viper.AddConfigPath(workDir + "/config")
	err := viper.ReadInConfig()
	if err != nil {
		panic("")
	}
}
