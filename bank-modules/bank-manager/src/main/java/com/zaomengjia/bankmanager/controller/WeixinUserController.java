package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.dto.WeixinUserDto;
import com.zaomengjia.bankmanager.service.UserService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.common.vo.user.WeixinUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 00:00
 */
@RestController
@RequestMapping("/weixin-user")
@Tag(name = "微信用户")
public class WeixinUserController {

    private final UserService userService;

    public WeixinUserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "获取列表")
    @GetMapping
    public ResultVO<?> getWeixinUser(@RequestParam int pageNum, @RequestParam int pageSize) {
        PageVO<WeixinUserVO> result = userService.getWeixinUserList(pageNum, pageSize);
        return ResultUtils.success(result);
    }

    @Operation(summary = "搜索")
    @GetMapping("/search")
    public ResultVO<?> searchWeixinUser(@RequestParam String keyword,
                                        @RequestParam @Parameter(description = "nickname - 根据昵称搜索; realName - 根据真实姓名搜索") String type,
                                        @RequestParam int pageNum,
                                        @RequestParam int pageSize) {
        PageVO<WeixinUserVO> result;

        switch (type) {
            case "nickname":
                result = userService.searchWeixinUserByNickname(keyword, pageNum, pageSize);
                break;
            case "realName":
                result = userService.searchWeixinUserByRealName(keyword, pageNum, pageSize);
                break;
            default:
                throw new AppException(ResultCode.PATTERN_ERROR);
        }
        return ResultUtils.success(result);
    }

    @Operation(summary = "更改")
    @PutMapping("/{userId}")
    public ResultVO<?> updateWeixinUser(@PathVariable String userId, @Validated @RequestBody WeixinUserDto dto) {
        userService.updateWeixinUser(userId, dto);
        return ResultUtils.success();
    }

    @Operation(summary = "设置审核状态")
    @PostMapping("/audit/{userId}")
    public ResultVO<?> updateWeixinUserAudit(@PathVariable String userId, @RequestParam @Parameter(description = "0为未审核，1为审核") int audit) {
        if(audit != 0 && audit != 1) {
            throw new AppException(ResultCode.PATTERN_ERROR);
        }
        userService.setWeixinUserAudit(userId, audit);
        return ResultUtils.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{userId}")
    public ResultVO<?> deleteWeixinUser(@PathVariable String userId) {
        userService.deleteWeixinUser(userId);
        return ResultUtils.success();
    }

}
