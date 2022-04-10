package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.OrderService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.entity.Order;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/getAllOrders/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllOrders(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return ResultUtils.success(orderService.getOrder(pageIndex,pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getOrderById/{id}")
    public ResultVO<?> getOrderById(@PathVariable String id){
        try{
            return ResultUtils.success(orderService.getOrderById(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getProductByProductName/{productName}")
    public ResultVO<?> getProductByProductName(@PathVariable String productName){
        try{
            return ResultUtils.success(orderService.getOrderByProductName(productName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getProductByUserName/{userName}")
    public ResultVO<?> getProductByPrice(@PathVariable String userName){
        try{
            return ResultUtils.success(orderService.getOrderByUserName(userName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/addOrder/{order}")
    public ResultVO<?> addOrder(@RequestBody Order order){
        try{
            orderService.addOrder(order);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResultVO<?> deleteOrder(@PathVariable String id){
        try{
            orderService.deleteOrder(id);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/updateOrder/{order}")
    public ResultVO<?> updateOrder(@RequestBody Order order){
        try{
            orderService.updateOrder(order);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
