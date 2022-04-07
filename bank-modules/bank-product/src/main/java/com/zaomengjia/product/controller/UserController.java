package com.zaomengjia.product.controller;

import com.alibaba.fastjson.JSONValidator;
import com.mysql.cj.util.StringUtils;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import com.zaomengjia.product.dto.UserInfoDto;
import com.zaomengjia.product.service.UserService;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/user")
    public ResultVO<?> uploadUserInfo(@Validated @RequestBody UserInfoDto userInfoDto) {
        userService.updateUserInfo(userInfoDto);
        return ResultUtils.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/user/{userId}")
    public ResultVO<?> getUserInfo(@PathVariable String userId) {
        if(StringUtils.isNullOrEmpty(userId)) {
            throw new AppException(ResultCode.PATTERN_ERROR);
        }

        WeixinUserVO userInfo = userService.getUserInfo(userId);
        return ResultUtils.success(userInfo);
    }
}
