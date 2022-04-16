package com.zaomengjia.bankmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.bankmanager.dto.FinancialProductDto;
import com.zaomengjia.bankmanager.service.FinancialProductService;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 20:39
 */
@Tag(name = "产品")
@RestController
@RequestMapping("/product")
public class FinancialProductController {

    private final FinancialProductService financialProductService;

    public FinancialProductController(FinancialProductService financialProductService) {
        this.financialProductService = financialProductService;
    }

    @Operation(summary = "获取所有理财产品")
    @GetMapping
    public ResultVO<?> getList(@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResultUtils.success(financialProductService.getList(pageNum, pageSize));
    }

    @Operation(summary = "根据名称搜索理财产品")
    @GetMapping("/search")
    public ResultVO<?> searchByName(@RequestParam String keyword, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResultUtils.success(financialProductService.searchByName(keyword, pageNum, pageSize));
    }

    @Operation(summary = "新增")
    @PostMapping
    public ResultVO<?> insert(@RequestBody FinancialProductDto dto) {
        String s = financialProductService.create(dto);
        JSONObject json = new JSONObject();
        json.put("financialProductId", s);
        return ResultUtils.success(json);
    }

    @Operation(summary = "修改")
    @PutMapping("/{financialProductId}")
    public ResultVO<?> modify(@PathVariable String financialProductId, @RequestBody FinancialProductDto dto) {
        financialProductService.modify(financialProductId, dto);
        return ResultUtils.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{financialProductId}")
    public ResultVO<?> delete(@PathVariable String financialProductId) {
        financialProductService.delete(financialProductId);
        return ResultUtils.success();
    }

}
