package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.OrderService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.pojo.Order;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/getAllOrders/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllOrders(@PathVariable int pageIndex,@PathVariable int pageSize){
        try{
            return ResultUtils.success(orderService.getOrder(pageIndex,pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @GetMapping("/getOrderById/{id}")
    public ResultVO<?> getOrderById(@PathVariable long id){
        try{
            return ResultUtils.success(orderService.getOrderById(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ORDER_ERROR,e.getMessage());
        }
    }

    @GetMapping("/getOrderByProductName/{productName}")
    public ResultVO<?> getOrderByProductName(@PathVariable String productName){
        try{
            return ResultUtils.success(orderService.getOrderByProductName(productName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ORDER_ERROR,e.getMessage());
        }
    }

    @GetMapping("/getOrderByUserName/{userName}")
    public ResultVO<?> getOrderByUserName(@PathVariable String userName){
        try{
            return ResultUtils.success(orderService.getOrderByUserName(userName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ORDER_ERROR,e.getMessage());
        }
    }

    @PostMapping("/addOrder/{order}")
    public ResultVO<?> addOrder(@RequestBody Order order){
        try{
            return ResultUtils.success(orderService.addOrder(order));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResultVO<?> deleteOrder(@PathVariable long id){
        try{
            return ResultUtils.success(orderService.deleteOrder(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @PutMapping("/updateOrder/{order}")
    public ResultVO<?> updateOrder(@RequestBody Order order){
        try{
            return ResultUtils.success(orderService.updateOrder(order));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}
