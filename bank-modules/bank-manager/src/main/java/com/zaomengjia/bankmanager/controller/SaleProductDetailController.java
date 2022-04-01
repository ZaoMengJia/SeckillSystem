package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.SaleProductDetailService;
import com.zaomengjia.common.message.Result;
import com.zaomengjia.common.pojo.SaleProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saleProductDetail")
public class SaleProductDetailController {
    @Autowired
    private SaleProductDetailService saleProductDetailService;

    @GetMapping("/getAllSaleProductDetail/{pageIndex}/{pageSize}")
    public Result getAllSaleProductDetail(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return Result.succ(saleProductDetailService.getSaleProductDetail(pageIndex, pageSize));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    @GetMapping("/getSaleProductDetailBySaid/{said}")
    public Result getSaleProductDetailBySaid(@PathVariable long said){
        try{
            return Result.succ(saleProductDetailService.getSaleProductDetailBySaid(said));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getSaleProductDetailByFpid/{fpid}")
    public Result getSaleProductDetailByFpid(@PathVariable long fpid){
        try{
            return Result.succ(saleProductDetailService.getSaleProductDetailByFpid(fpid));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getSaleProductDetailBySaidAndFpid/{said/{fpid}")
    public Result getSaleProductDetailByFpid(@PathVariable long said,@PathVariable long fpid){
        try{
            return Result.succ(saleProductDetailService.getSaleProductDetailBySaidAndFpid(said,fpid));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }



    @PostMapping("/addSaleProductDetail/{detail}")
    public Result addSaleProductDetail(@RequestBody SaleProductDetail detail){
        try{
            return Result.succ(saleProductDetailService.addSaleProductDetail(detail));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @DeleteMapping("/deleteSaleProductDetail/{said}/{fpid}")
    public Result deleteSaleProductDetail(@PathVariable long said,@PathVariable long fpid){
        try{
            return Result.succ(saleProductDetailService.deleteSaleProductDetail(said,fpid));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @PutMapping("/updateSaleProductDetail/{detail}")
    public Result updateSaleProductDetail(@RequestBody SaleProductDetail detail){
        try{
            return Result.succ(saleProductDetailService.updateSaleProductDetail(detail));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }
}
