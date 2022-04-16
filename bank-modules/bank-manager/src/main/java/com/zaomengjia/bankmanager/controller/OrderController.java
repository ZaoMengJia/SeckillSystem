package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.OrderService;
import com.zaomengjia.bankmanager.service.impl.OrderServiceImpl;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 20:22
 */
@Tag(name = "订单")
@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "获取所有订单")
    @GetMapping
    public ResultVO<?> getOrderList(@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResultUtils.success(orderService.getOrderList(pageNum, pageSize));
    }

    @Operation(summary = "根据订单号搜索")
    @GetMapping("/search")
    public ResultVO<?> searchOrder(@RequestParam String keyword, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResultUtils.success(orderService.searchOrderById(keyword, pageNum, pageSize));
    }
}
