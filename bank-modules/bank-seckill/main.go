package main

import (
	"bank-seckill/common"
	"bank-seckill/routes"
	"github.com/gin-gonic/gin"
	"github.com/spf13/viper"
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
	r = routes.CollectRoute(r)

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
	viper.SetConfigFile(workDir + "/application.yml")
	viper.ReadInConfig()

	active := viper.GetString("active")
	viper.SetConfigFile(workDir + "/application-" + active + ".yml")
	err := viper.ReadInConfig()
	if err != nil {
		panic(err)
	}
}
