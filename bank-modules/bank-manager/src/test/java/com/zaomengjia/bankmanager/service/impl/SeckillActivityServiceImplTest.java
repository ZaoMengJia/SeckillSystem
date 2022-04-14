package com.zaomengjia.bankmanager.service.impl;

import com.zaomengjia.bankmanager.service.SeckillActivityService;
import com.zaomengjia.bankmanager.service.UserService;
import com.zaomengjia.common.dao.SeckillActivityMapper;
import com.zaomengjia.common.entity.SeckillActivity;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/14 15:26
 */
@SpringBootTest
class SeckillActivityServiceImplTest {

    @Autowired
    private UserService service;

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Test
    void test() {
        PageVO<WeixinUserVO> weixinUserList = service.getWeixinUserList(1, 100);
        System.out.println(weixinUserList);
    }
}