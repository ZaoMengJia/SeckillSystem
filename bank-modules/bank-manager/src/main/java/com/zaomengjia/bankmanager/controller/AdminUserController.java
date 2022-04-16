package com.zaomengjia.bankmanager.controller;

import cn.hutool.jwt.JWT;
import com.zaomengjia.bankmanager.service.UserService;
import com.zaomengjia.common.constant.RequestHeaderKey;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.bankmanager.dto.AdminUserDto;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.common.vo.user.AdminUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员用户Controller
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/8 00:13
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员用户")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "获取列表")
    @GetMapping
    public ResultVO<?> getAdminUserList(@RequestParam int pageNum, @RequestParam int pageSize) {
        PageVO<AdminUserVO> result = userService.getAdminUserList(pageNum, pageSize);
        return ResultUtils.success(result);
    }

    @Operation(summary = "判断管理员是否存在")
    @GetMapping("/exist")
    public ResultVO<?> adminUserExist(@RequestParam String name){
        return ResultUtils.success(userService.getAdminUserByUsername(name) != null);
    }

    @Operation(summary = "通过userId获取")
    @GetMapping("/{userId}")
    public ResultVO<?> getAdminUserById(@PathVariable String userId) {
        AdminUserVO adminUser = userService.getAdminUserById(userId);
        return ResultUtils.success(adminUser);
    }

    @Operation(summary = "通过名字精确查找")
    @GetMapping("/query")
    public ResultVO<?> getAdminUserByName(@RequestParam String name) {
        AdminUserVO admin = userService.getAdminUserByUsername(name);
        return ResultUtils.success(admin);
    }

    @Operation(summary = "通过关键词模糊查找")
    @GetMapping("/search")
    public ResultVO<?> searchAdminUserByKeyword(@RequestParam String keyword, @RequestParam int pageNum, @RequestParam int pageSize) {
        PageVO<AdminUserVO> result = userService.searchAdminUserByUsername(keyword, pageNum, pageSize);
        return ResultUtils.success(result);
    }


    @Operation(summary = "新增")
    @PostMapping
    public ResultVO<?> insertAdminUser(@Validated @RequestBody AdminUserDto dto) {
        String userId = userService.insertAdminUser(dto);
        return ResultUtils.success(userId);
    }

    @Operation(summary = "更新")
    @PutMapping("/{userId}")
    public ResultVO<?> updateAdminUser(@PathVariable String userId, @Validated @RequestBody AdminUserDto dto, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        //验证token
        String jwtUserId = JWT.of(token).getPayload().getClaimsJson().getStr("userId");
        if(!jwtUserId.equals(userId)) {
            //不能更改别人的密码
            dto.setPassword(null);
        }

        userService.updateAdminUser(userId, dto);
        return ResultUtils.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{userId}")
    public ResultVO<?> deleteAdminUser(@PathVariable String userId, @RequestHeader(RequestHeaderKey.AUTHORIZATION) String token) {
        //验证token
        String jwtUserId = JWT.of(token).getPayload().getClaimsJson().getStr("userId");
        if(jwtUserId.equals(userId)) {
            throw new AppException(ResultCode.CANNOT_DELETE_SELF);
        }

        userService.deleteAdminUser(userId);
        return ResultUtils.success();
    }

}
