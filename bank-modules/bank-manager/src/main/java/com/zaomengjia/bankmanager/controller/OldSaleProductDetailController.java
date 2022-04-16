//package com.zaomengjia.bankmanager.controller;
//
//import com.zaomengjia.bankmanager.service.SaleProductDetailService;
//import com.zaomengjia.common.constant.ResultCode;
//import com.zaomengjia.common.entity.SaleProductDetail;
//import com.zaomengjia.common.utils.ResultUtils;
//import com.zaomengjia.common.vo.ResultVO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/saleProductDetail")
//public class OldSaleProductDetailController {
//    @Autowired
//    private SaleProductDetailService saleProductDetailService;
//
//    @GetMapping("/getAllSaleProductDetail/{pageIndex}/{pageSize}")
//    public ResultVO<?> getAllSaleProductDetail(@PathVariable int pageIndex, @PathVariable int pageSize){
//        try{
//            return ResultUtils.success(saleProductDetailService.getSaleProductDetail(pageIndex, pageSize));
//        }catch (Exception e){
//            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//
//    @GetMapping("/getSaleProductDetailBySaid/{said}")
//    public ResultVO<?> getSaleProductDetailBySaid(@PathVariable String said){
//        try{
//            return ResultUtils.success(saleProductDetailService.getSaleProductDetailBySaid(said));
//        }catch (Exception e){
//            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//
//    @GetMapping("/getSaleProductDetailByFpid/{fpid}")
//    public ResultVO<?> getSaleProductDetailByFpid(@PathVariable String fpid){
//        try{
//            return ResultUtils.success(saleProductDetailService.getSaleProductDetailByFpid(fpid));
//        }catch (Exception e){
//            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//
//    @GetMapping("/getSaleProductDetailBySaidAndFpid/{said}/{fpid}")
//    public ResultVO<?> getSaleProductDetailBySaidAndFpid(@PathVariable String said,@PathVariable String fpid){
//        try{
//            return ResultUtils.success(saleProductDetailService.getSaleProductDetailBySaidAndFpid(said,fpid));
//        }catch (Exception e){
//            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//
//
//
//    @PostMapping("/addSaleProductDetail/{detail}")
//    public ResultVO<?> addSaleProductDetail(@RequestBody SaleProductDetail detail){
//        try{
//            saleProductDetailService.addSaleProductDetail(detail);
//            return ResultUtils.success();
//        }catch (Exception e){
//            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/deleteSaleProductDetail/{said}/{fpid}")
//    public ResultVO<?> deleteSaleProductDetail(@PathVariable String said,@PathVariable String fpid){
//        try{
//            saleProductDetailService.deleteSaleProductDetail(said, fpid);
//            return ResultUtils.success();
//        }catch (Exception e){
//            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//
//    @PutMapping("/updateSaleProductDetail/{detail}")
//    public ResultVO<?> updateSaleProductDetail(@RequestBody SaleProductDetail detail){
//        try{
//            saleProductDetailService.updateSaleProductDetail(detail);
//            return ResultUtils.success();
//        }catch (Exception e){
//            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//}
