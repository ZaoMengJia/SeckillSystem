package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.OrderService;
import com.zaomengjia.common.message.Result;
import com.zaomengjia.common.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/getAllOrders/{pageIndex}/{pageSize}")
    public Result getAllOrders(@PathVariable int pageIndex,@PathVariable int pageSize){
        try{
            return Result.succ(orderService.getOrder(pageIndex,pageSize));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getOrderById/{id}")
    public Result getOrderById(@PathVariable long id){
        try{
            return Result.succ(orderService.getOrderById(id));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getProductByProductName/{productName}")
    public Result getProductByProductName(@PathVariable String productName){
        try{
            return Result.succ(orderService.getOrderByProductName(productName));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getProductByUserName/{userName}")
    public Result getProductByPrice(@PathVariable String userName){
        try{
            return Result.succ(orderService.getOrderByUserName(userName));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @PostMapping("/addOrder/{order}")
    public Result addOrder(@RequestBody Order order){
        try{
            return Result.succ(orderService.addOrder(order));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @DeleteMapping("/deleteOrder/{id}")
    public Result deleteOrder(@PathVariable long id){
        try{
            return Result.succ(orderService.deleteOrder(id));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @PutMapping("/updateOrder/{order}")
    public Result updateOrder(@RequestBody Order order){
        try{
            return Result.succ(orderService.updateOrder(order));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }
}
