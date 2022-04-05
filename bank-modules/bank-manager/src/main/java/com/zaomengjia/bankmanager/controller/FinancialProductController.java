package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.FinancialProductService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.pojo.FinancialProduct;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/financialProduct")
public class FinancialProductController {

    private final FinancialProductService financialProductService;

    public FinancialProductController(FinancialProductService financialProductService) {
        this.financialProductService = financialProductService;
    }

    @GetMapping("/productExist/{productName}")
    public ResultVO<?> productExist(@PathVariable String productName){
        try{
            return ResultUtils.success(financialProductService.financialProductExist(productName));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_ACCOUNT_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getAllProduct/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllProduct(@PathVariable int pageIndex,@PathVariable int pageSize){
        try{
            return ResultUtils.success(financialProductService.getFinancialProduct(pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @GetMapping(value = "/searchProduct/{keyword}/{pageIndex}/{pageSize}")
    public ResultVO<?> searchProduct(@PathVariable String keyword,@PathVariable int pageIndex,@PathVariable int pageSize){
        try{
            return ResultUtils.success(financialProductService.searchProduct(keyword, pageIndex, pageSize));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getProductById/{id}")
    public ResultVO<?> getProductById(@PathVariable long id){
        try{
            return ResultUtils.success(financialProductService.getFinancialProductById(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_PRODUCT_ERROR,e.getMessage());
        }
    }

    @GetMapping("/getProductByName/{name}")
    public ResultVO<?> getProductByName(@PathVariable String name){
        try{
            return ResultUtils.success(financialProductService.getFinancialProductByName(name));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_PRODUCT_ERROR,e.getMessage());
        }
    }

    @GetMapping("/getProductByPrice/{price}")
    public ResultVO<?> getProductByPrice(@PathVariable int price){
        try{
            return ResultUtils.success(financialProductService.getFinancialProductByPrice(price));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.NO_SUCH_PRODUCT_ERROR,e.getMessage());
        }
    }

    @PostMapping("/addProduct")
    public ResultVO<?> addProduct(@RequestBody FinancialProduct product){
        try{
            return ResultUtils.success(financialProductService.addFinancialProduct(product));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResultVO<?> deleteProduct(@PathVariable long id){
        try{
            return ResultUtils.success(financialProductService.deleteFinancialProduct(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @PutMapping("/updateProduct")
    public ResultVO<?> updateProduct(@RequestBody FinancialProduct product){
        try{
            return ResultUtils.success(financialProductService.updateFinancialProduct(product));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}
