package com.zaomengjia.order.controller;

import cn.hutool.jwt.JWT;
import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.common.constant.OrderStatus;
import com.zaomengjia.common.constant.RequestHeaderKey;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.vo.bank.OrderVO;
import com.zaomengjia.common.vo.page.PageVO;
import com.zaomengjia.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/11 00:24
 */
@RestController
@Tag(name = "订单")
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{orderId}")
    public ResultVO<?> getOrderDetail(@PathVariable String orderId) {
        return ResultUtils.success(orderService.getOrderDetail(orderId));
    }

    @Operation(summary = "订单状态")
    @GetMapping("/status/{orderId}")
    public ResultVO<?> getOrderStatus(@PathVariable String orderId) {
        OrderStatus orderStatus = orderService.getOrderStatus(orderId);
        JSONObject json = new JSONObject();
        json.put("status", orderStatus);
        return ResultUtils.success(json);
    }
}
