package common

import (
	"github.com/nacos-group/nacos-sdk-go/clients"
	"github.com/nacos-group/nacos-sdk-go/common/constant"
	"github.com/nacos-group/nacos-sdk-go/vo"
	"github.com/spf13/viper"
)

func NacosInit() {

	clientConfig := constant.ClientConfig{
		NamespaceId:         "",
		TimeoutMs:           5000,
		NotLoadCacheAtStart: true,
		LogDir:              "log",
		CacheDir:            "cache",
		LogLevel:            "debug",
	}

	serverConfigs := []constant.ServerConfig{
		{
			IpAddr:      viper.GetString("nacos.url"),
			ContextPath: "/nacos",
			Port:        viper.GetUint64("nacos.port"),
			Scheme:      "http",
		},
	}

	namingClient, _ := clients.NewNamingClient(
		vo.NacosClientParam{
			ClientConfig:  &clientConfig,
			ServerConfigs: serverConfigs,
		},
	)

	namingClient.RegisterInstance(vo.RegisterInstanceParam{
		Ip:          viper.GetString("nacos.url"),
		Port:        viper.GetUint64("nacos.port"),
		ServiceName: "bank-seckill",
		Weight:      10,
		Enable:      true,
		Healthy:     true,
		Ephemeral:   true,
		Metadata:    map[string]string{"idc": "shanghai"},
		ClusterName: "DEFAULT",
		GroupName:   "DEFAULT_GROUP",
	})

}
