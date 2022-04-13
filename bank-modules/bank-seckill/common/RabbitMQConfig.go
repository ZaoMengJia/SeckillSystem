package common

import (
	"github.com/spf13/viper"
	"github.com/wagslane/go-rabbitmq"
)

var MQPublisher *rabbitmq.Publisher

func InitRabbitMQ() {
	username := viper.GetString("rabbitmq.username")
	password := viper.GetString("rabbitmq.password")
	url := viper.GetString("rabbitmq.localhost")

	publisher, err := rabbitmq.NewPublisher(
		"amqp://"+username+":"+password+"@"+url, rabbitmq.Config{},
	)

	if err != nil {
		panic(err)
	}

	MQPublisher = publisher
}
