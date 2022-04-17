package com.zaomengjia.stock.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.jwt.JWT;
import com.alibaba.fastjson.JSON;
import com.zaomengjia.common.dao.WeixinUserMapper;
import com.zaomengjia.common.entity.User;
import com.zaomengjia.common.entity.WeixinUser;
import com.zaomengjia.common.service.StockSimpleService;
import com.zaomengjia.common.utils.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 17:04
 */
@SpringBootTest
class MQServiceImplTest {

}