package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.FinancialProductService;
import com.zaomengjia.common.message.Result;
import com.zaomengjia.common.pojo.FinancialProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/financialProduct")
public class FinancialProductController {
    @Autowired
    private FinancialProductService financialProductService;

    @GetMapping("/getAllProduct/{pageIndex}/{pageSize}")
    public Result getAllProduct(@PathVariable int pageIndex,@PathVariable int pageSize){
        try{
            return Result.succ(financialProductService.getFinancialProduct(pageIndex, pageSize));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    @GetMapping("/getProductById/{id}")
    public Result getProductById(@PathVariable long id){
        try{
            return Result.succ(financialProductService.getFinancialProductById(id));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getProductByName/{name}")
    public Result getProductByName(@PathVariable String name){
        try{
            return Result.succ(financialProductService.getFinancialProductByName(name));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getProductByPrice/{price}")
    public Result getProductByPrice(@PathVariable int price){
        try{
            return Result.succ(financialProductService.getFinancialProductByPrice(price));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @PostMapping("/addProduct/{product}")
    public Result addProduct(@RequestBody FinancialProduct product){
        try{
            return Result.succ(financialProductService.addFinancialProduct(product));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public Result deleteProduct(@PathVariable long id){
        try{
            return Result.succ(financialProductService.deleteFinancialProduct(id));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @PutMapping("/updateProduct/{product}")
    public Result updateProduct(@RequestBody FinancialProduct product){
        try{
            return Result.succ(financialProductService.updateFinancialProduct(product));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }
}
