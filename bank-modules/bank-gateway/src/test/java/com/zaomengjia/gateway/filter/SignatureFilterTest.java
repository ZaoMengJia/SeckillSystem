package com.zaomengjia.gateway.filter;

import com.zaomengjia.common.utils.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/11 18:44
 */
@SpringBootTest
class SignatureFilterTest {


    @Test
    void signatureTest() {
        String s = MD5Utils.toMD5("{\"name\":\"12\",\"detail\":\"3241234\",\"beginTime\":\"2022-04-04T16:00:00.000Z\",\"endTime\":\"2022-05-12T16:00:00.000Z\",\"image\":\"https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/MMX62?wid=540&hei=540&align=0,-1&fmt=jpeg&qlt=90&fit=constrain&.v=1472256277309\"}273dc747253e4d8eaf4534d6984b77c71650099332994IZe37zbJnGpPrZ5u");
        System.out.println(s);
    }
}