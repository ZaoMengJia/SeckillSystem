package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.SaleProductDetailService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.pojo.SaleProductDetail;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saleProductDetail")
public class SaleProductDetailController {
    @Autowired
    private SaleProductDetailService saleProductDetailService;

    @GetMapping("/getAllSaleProductDetail/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllSaleProductDetail(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return ResultUtils.success(saleProductDetailService.getSaleProductDetail(pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getSaleProductDetailBySaid/{said}")
    public ResultVO<?> getSaleProductDetailBySaid(@PathVariable long said){
        try{
            return ResultUtils.success(saleProductDetailService.getSaleProductDetailBySaid(said));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getSaleProductDetailByFpid/{fpid}")
    public ResultVO<?> getSaleProductDetailByFpid(@PathVariable long fpid){
        try{
            return ResultUtils.success(saleProductDetailService.getSaleProductDetailByFpid(fpid));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getSaleProductDetailBySaidAndFpid/{said}/{fpid}")
    public ResultVO<?> getSaleProductDetailBySaidAndFpid(@PathVariable long said,@PathVariable long fpid){
        try{
            return ResultUtils.success(saleProductDetailService.getSaleProductDetailBySaidAndFpid(said,fpid));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



    @PostMapping("/addSaleProductDetail/{detail}")
    public ResultVO<?> addSaleProductDetail(@RequestBody SaleProductDetail detail){
        try{
            return ResultUtils.success(saleProductDetailService.addSaleProductDetail(detail));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/deleteSaleProductDetail/{said}/{fpid}")
    public ResultVO<?> deleteSaleProductDetail(@PathVariable long said,@PathVariable long fpid){
        try{
            return ResultUtils.success(saleProductDetailService.deleteSaleProductDetail(said,fpid));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/updateSaleProductDetail/{detail}")
    public ResultVO<?> updateSaleProductDetail(@RequestBody SaleProductDetail detail){
        try{
            return ResultUtils.success(saleProductDetailService.updateSaleProductDetail(detail));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
