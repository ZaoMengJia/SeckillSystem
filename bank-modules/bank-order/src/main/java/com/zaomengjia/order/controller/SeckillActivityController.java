package com.zaomengjia.order.controller;

import cn.hutool.jwt.JWT;
import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.constant.RequestHeaderKey;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.MD5Utils;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.vo.bank.SeckillActivityDetailVO;
import com.zaomengjia.common.vo.bank.SeckillActivityVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.order.service.OrderService;
import com.zaomengjia.order.service.SeckillService;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/10 17:32
 */
@RestController
@Tag(name = "抢购活动")
@RequestMapping("/sec-kill")
public class SeckillActivityController {

    private final SeckillService seckillService;

    private final OrderService orderService;

    @Value("${sec-kill.url.key}")
    private String key;

    public SeckillActivityController(
            SeckillService seckillService,
            OrderService orderService
    ) {
        this.seckillService = seckillService;
        this.orderService = orderService;
    }

    @Operation(summary = "抢购链接")
    @GetMapping("/url/{seckillActivityId}")
    public ResultVO<?> getSeckillUrl(@PathVariable String seckillActivityId, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        String userId = (String) JWT.of(token).getPayload().getClaim("userId");
        if(StringUtil.isNullOrEmpty(seckillActivityId)) {
            throw new AppException(ResultCode.PATTERN_ERROR);
        }

        SeckillActivityVO activity = seckillService.getSeckillActivityById(seckillActivityId);
        if(activity == null) {
            //用于迷惑非法请求者
            throw new AppException(ResultCode.ACTIVITY_NOT_STARTED);
        }
        else if(activity.getBeginTime().before(new Date())) {
            throw new AppException(ResultCode.ACTIVITY_NOT_STARTED);
        }

        String path = generateSeckillPath(seckillActivityId, userId);
        JSONObject json = new JSONObject();
        json.put("path", "/" + path);
        return ResultUtils.success(json);
    }

    @Operation(summary = "抢购活动列表")
    @GetMapping("/list")
    public ResultVO<?> getSeckillList(@RequestParam int pageNum, @RequestParam int pageSize) {
        PageVO<SeckillActivityVO> list = seckillService.getSeckillActivityList(pageNum, pageSize);
        return ResultUtils.success(list);
    }

    @Operation(summary = "抢购活动详情")
    @GetMapping("/{seckillActivityId}")
    public ResultVO<?> getSeckillDetail(@PathVariable String seckillActivityId) {
        SeckillActivityDetailVO seckillActivityDetail = seckillService.getSeckillActivityDetail(seckillActivityId);
        if(seckillActivityDetail == null) {
            throw new AppException(ResultCode.NO_SUCH_ACTIVITY_ERROR);
        }
        return ResultUtils.success(seckillActivityDetail);
    }

    @Operation(summary = "抢购")
    @PostMapping("/{path}")
    public ResultVO<?> seckill(@PathVariable @Parameter(description = "抢购链接中的随机参数") String path,
                               @RequestParam String seckillActivityId, @RequestParam String financialProductId, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        String userId = (String) JWT.of(token).getPayload().getClaim("userId");
        String correctPath = generateSeckillPath(seckillActivityId, userId);
        if(!correctPath.equals(path)) {
            //迷惑非法接口调用者
            throw new AppException(ResultCode.ACTIVITY_NOT_STARTED);
        }

        String orderId = orderService.createOrder(userId, seckillActivityId, financialProductId);
        JSONObject json = new JSONObject();
        json.put("orderId", orderId);
        return ResultUtils.success(json);
    }

    private String generateSeckillPath(String seckillActivityId, String userId) {
        return MD5Utils.toMD5(seckillActivityId + userId + key).toLowerCase();
    }

}
