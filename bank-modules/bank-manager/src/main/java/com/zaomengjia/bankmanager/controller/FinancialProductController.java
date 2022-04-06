package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.FinancialProductService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.pojo.FinancialProduct;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/financialProduct")
public class FinancialProductController {
    @Autowired
    private FinancialProductService financialProductService;

    @GetMapping("/getAllProduct/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllProduct(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return ResultUtils.success(financialProductService.getFinancialProduct(pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getProductById/{id}")
    public ResultVO<?> getProductById(@PathVariable long id){
        try{
            return ResultUtils.success(financialProductService.getFinancialProductById(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getProductByName/{name}")
    public ResultVO<?> getProductByName(@PathVariable String name){
        try{
            return ResultUtils.success(financialProductService.getFinancialProductByName(name));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getProductByPrice/{price}")
    public ResultVO<?> getProductByPrice(@PathVariable int price){
        try{
            return ResultUtils.success(financialProductService.getFinancialProductByPrice(price));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/addProduct/{product}")
    public ResultVO<?> addProduct(@RequestBody FinancialProduct product){
        try{
            return ResultUtils.success(financialProductService.addFinancialProduct(product));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResultVO<?> deleteProduct(@PathVariable long id){
        try{
            return ResultUtils.success(financialProductService.deleteFinancialProduct(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/updateProduct/{product}")
    public ResultVO<?> updateProduct(@RequestBody FinancialProduct product){
        try{
            return ResultUtils.success(financialProductService.updateFinancialProduct(product));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
