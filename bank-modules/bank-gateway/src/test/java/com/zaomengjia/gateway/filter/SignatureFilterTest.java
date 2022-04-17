//package com.zaomengjia.gateway.filter;
//
//import cn.hutool.core.codec.Base64;
//import cn.hutool.core.lang.UUID;
//import cn.hutool.core.net.URLDecoder;
//import cn.hutool.core.text.csv.CsvWriter;
//import com.alibaba.fastjson.JSON;
//import com.zaomengjia.common.dao.WeixinUserMapper;
//import com.zaomengjia.common.entity.WeixinUser;
//import com.zaomengjia.common.service.StockSimpleService;
//import com.zaomengjia.common.utils.MD5Utils;
//import com.zaomengjia.common.utils.RedisUtils;
//import com.zaomengjia.gateway.constant.AuthorityGroup;
//import com.zaomengjia.gateway.utils.JwtUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import java.nio.charset.StandardCharsets;
//import java.text.MessageFormat;
//import java.util.*;
//import java.util.stream.IntStream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * @author orangeboyChen
// * @version 1.0
// * @date 2022/4/11 18:44
// */
//@SpringBootTest
//class SignatureFilterTest {
//
//
//    @Autowired
//    private StockSimpleService stockSimpleService;
//
//    @Autowired
//    private WeixinUserMapper weixinUserMapper;
//
//    @Autowired
//    private RedisUtils redisUtils;
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private EntityManager entityManager;
//
//
//
//    @Test
//    @Transactional
//    @Rollback(value = false)
//    void test2() {
//        String activityId = "2c9180858036b5a8018036b8e2ae0000";
//        String productId = "2c9180858036b5a8018036b9c9150001";
//        CsvWriter csvWriter = new CsvWriter("//Users/orangeboychen/Desktop/Project.nosync/SeckillSystem/info.csv");
//        csvWriter.writeLine("Authorization", "timestamp", "nonce", "signature", "path");
//
//        List<WeixinUser> userList = new ArrayList<>();
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("insert `weixin_user`(`id`,audit,avatar_url,create_time,deleted,gender,has_job,id_card,is_discredit,nickname,openid,overdue_record,real_name,update_time) values");
//
//        IntStream.rangeClosed(1, 100000).forEach(c -> {
//            WeixinUser weixinUser = new WeixinUser();
//            weixinUser.setId(UUID.fastUUID().toString(true));
//            weixinUser.setOpenid(UUID.fastUUID().toString(true));
//            weixinUser.setAudit(1);
//            userList.add(weixinUser);
//        });
//
//        List<String> lines = new ArrayList<>();
//        userList.stream().parallel().forEach(weixinUser -> {
//            sb.append(String.format("('%s',%s,'%s',%s,%s,%s,%s,'%s',%s,'%s','%s',%s,'%s',%s),", weixinUser.getId(), "1", "", "null", "0", "0", "null", "23333", "false", "test", weixinUser.getOpenid(), "false", "Test", "null"));
//
//            String userId = weixinUser.getId();
//            String path = MD5Utils.toMD5(activityId + userId + "33oWwIws6iQnZWZc");
//
//
//            TreeMap<String, String> map = new TreeMap<>();
//            map.put("seckillActivityId", activityId);
//            map.put("financialProductId", productId);
//
//            String timestamp = String.valueOf(System.currentTimeMillis());
//            String nonce = UUID.fastUUID().toString(true);
//            String input = JSON.toJSONString(new TreeMap<>(map));
//            String signature = getSignature(input + nonce + timestamp + "IZe37zbJnGpPrZ5u");
//
//            String jwt = "Bearer " + jwtUtils.getWeixinJwt(Collections.singletonList(AuthorityGroup.USER), userId, 1);
//            redisUtils.set("user-audit::" + userId, 1);
//
//
//            lines.add(String.format("%s,%s,%s,%s,%s", jwt, timestamp, nonce, signature, path));
//
//        });
//
//        csvWriter.write(lines);
//        String query = sb.toString();
//        query = query.substring(0, query.length() - 1);
//        System.out.println(query);
//        entityManager.createNativeQuery(query).executeUpdate();
//
////        weixinUserMapper.saveAll(userList);
//        csvWriter.flush();
//        csvWriter.close();
//    }
//
//
//    private String getSignature(String input) {
//        input = URLDecoder.decode(input, StandardCharsets.UTF_8);
//        String md5 = MD5Utils.toMD5(input);
//        return Base64.encode(md5).replaceAll("[=/+]", "");
//    }
//}