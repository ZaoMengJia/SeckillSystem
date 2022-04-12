package com.zaomengjia.order.controller;

import cn.hutool.jwt.JWT;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import com.zaomengjia.common.constant.RequestHeaderKey;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.bank.OrderVO;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import com.zaomengjia.order.dto.UserInfoDto;
import com.zaomengjia.order.service.OrderService;
import com.zaomengjia.order.service.UserService;
import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/7 21:46
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户")
public class UserController {

    private final UserService userService;

    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/{userId}")
    public ResultVO<?> uploadUserInfo(@PathVariable String userId, @Validated @RequestBody UserInfoDto userInfoDto, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        String tokenUserId = (String) JWT.of(token).getPayload().getClaim("userId");
        if(!tokenUserId.equals(userId)) {
            throw new AppException(ResultCode.INVALID_REQUEST_ERROR);
        }

        userService.updateUserInfo(userInfoDto, userId);
        return ResultUtils.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/{userId}")
    public ResultVO<?> getUserInfo(@PathVariable String userId, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        String tokenUserId = (String) JWT.of(token).getPayload().getClaim("userId");
        if(!tokenUserId.equals(userId)) {
            throw new AppException(ResultCode.INVALID_REQUEST_ERROR);
        }

        WeixinUserVO userInfo = userService.getUserInfo(userId);
        return ResultUtils.success(userInfo);
    }

    @Operation(summary = "用户订单", tags = "订单")
    @GetMapping("/order/{userId}")
    public ResultVO<?> getUserOrder(@PathVariable String userId, @RequestParam int pageNum, @RequestParam int pageSize, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        String tokenUserId = (String) JWT.of(token).getPayload().getClaim("userId");
        if(!tokenUserId.equals(userId)) {
            throw new AppException(ResultCode.INVALID_REQUEST_ERROR);
        }

        PageVO<OrderVO> userOrderList = orderService.getUserOrderList(userId, pageNum, pageSize);
        return ResultUtils.success(userOrderList);
    }

    @Operation(summary = "用户状态")
    @GetMapping("/status/{userId}")
    public ResultVO<?> getUserStatus(@PathVariable String userId, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        String tokenUserId = (String) JWT.of(token).getPayload().getClaim("userId");
        if(!tokenUserId.equals(userId)) {
            throw new AppException(ResultCode.INVALID_REQUEST_ERROR);
        }

        boolean isAudited = false;
        boolean isRegistered = false;

        WeixinUserVO userInfo = userService.getUserInfo(userId);
        if(!StringUtil.isNullOrEmpty(userInfo.getIdCard())) {
            isRegistered = true;
        }

        //Todo: 审核还没写
        isAudited = true;

        JSONObject json = new JSONObject();
        json.put("isAudited", isAudited);
        json.put("isRegistered", isRegistered);
        return ResultUtils.success(json);
    }
}
