# 银行秒杀系统

## 项目结构
```
.
├── bank-common 公共类 com.zaomengjia.common.*
│   └── pom.xml
├── bank-modules 模块
│   ├── bank-gateway 网关模块 com.zaomengjia.gateway.*
│   │   ├── ...
│   │   └── pom.xml
│   ├── bank-manager 管理员模块 com.zaomengjia.bankmanager.*
│   │   ├── ...
│   │   └── pom.xml
│   ├── bank-order 订单模块 com.zaomengjia.order.*
│   │   ├── ...
│   │   └── pom.xml
│   └── bank-stock 库存模块 com.zaomengjia.stock.*
│       ├── ...
│       └── pom.xml
│
├── docker-compose 测试环境用的docker-compose
│   ├── docker-compose-deploy-dev.yml PC/Mac x86用
│   └── docker-compose-deploy-dev-mac-m1.yml Mac M1用
│
├── .github Github Action配置文件
│
└── pom.xml
```

### 部署测试环境
```shell
docker compose -f <文件> -d up 
```