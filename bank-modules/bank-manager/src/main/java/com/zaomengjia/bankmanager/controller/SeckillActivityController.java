package com.zaomengjia.bankmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.zaomengjia.bankmanager.dto.SaleProductDto;
import com.zaomengjia.bankmanager.dto.SeckillActivityDto;
import com.zaomengjia.bankmanager.service.SaleProductService;
import com.zaomengjia.bankmanager.service.SeckillActivityService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.exception.AppException;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import com.zaomengjia.common.vo.bank.SaleProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author orangeboyChen
 * @version 1.0
 * @date 2022/4/15 20:27
 */
@Tag(name = "抢购活动")
@RequestMapping("/seckill")
@RestController
public class SeckillActivityController {

    private final SeckillActivityService seckillActivityService;

    private final SaleProductService saleProductService;

    public SeckillActivityController(SeckillActivityService seckillActivityService, SaleProductService saleProductService) {
        this.seckillActivityService = seckillActivityService;
        this.saleProductService = saleProductService;
    }

    @GetMapping
    @Operation(summary = "获取所有活动")
    public ResultVO<?> getSeckillActivityList(@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResultUtils.success(seckillActivityService.getListWithDetail(pageNum, pageSize));
    }

    @GetMapping("/detail")
    @Operation(summary = "获取所有活动，包括活动下的商品")
    public ResultVO<?> getSeckillActivityListWithDetail(@RequestParam int pageNum, @RequestParam int pageSize) {
        return ResultUtils.success(seckillActivityService.getListWithDetail(pageNum, pageSize));
    }

    @GetMapping("/detail/{seckillActivityId}")
    @Operation(summary = "某个活动下的详情信息")
    public ResultVO<?> getSeckillActivityListWithDetail(@PathVariable String seckillActivityId) {
        return ResultUtils.success(seckillActivityService.getDetail(seckillActivityId));
    }

    @GetMapping("/search")
    @Operation(summary = "根据活动名模糊查找")
    public ResultVO<?> searchSeckillActivity(@RequestParam String keyword, @RequestParam int pageNum, @RequestParam int pageSize) {
        return ResultUtils.success(seckillActivityService.searchByName(keyword, pageNum, pageSize));
    }

    @PostMapping
    @Operation(summary = "新建")
    public ResultVO<?> createSeckillActivity(@RequestBody SeckillActivityDto dto) {
        String s = seckillActivityService.create(dto);
        JSONObject json = new JSONObject();
        json.put("seckillActivityId", s);
        return ResultUtils.success(json);
    }

    @PutMapping("/{seckillActivityId}")
    @Operation(summary = "修改")
    public ResultVO<?> modifySeckillActivity(@PathVariable String seckillActivityId, @RequestBody SeckillActivityDto dto) {
        seckillActivityService.modify(seckillActivityId, dto);
        return ResultUtils.success();
    }

    @DeleteMapping("/{seckillActivityId}")
    @Operation(summary = "删除")
    public ResultVO<?> deleteSeckillActivity(@PathVariable String seckillActivityId) {
        seckillActivityService.delete(seckillActivityId);
        return ResultUtils.success();
    }

    @GetMapping("/product/{seckillActivityId}")
    @Operation(summary = "获取活动下的商品")
    public ResultVO<?> getProductList(@PathVariable String seckillActivityId) {
        List<SaleProductVO> list = saleProductService.getList(seckillActivityId);
        return ResultUtils.success(list);
    }

    @PostMapping("/product/{seckillActivityId}/{financialProductId}")
    @Operation(summary = "添加商品")
    public ResultVO<?> insertProduct(@PathVariable String seckillActivityId, @PathVariable String financialProductId, @RequestBody SaleProductDto dto) {
        if(dto.getTotal() < 0 || dto.getQuantity() < 0 || dto.getQuantity() > dto.getTotal()) {
            throw new AppException(ResultCode.PATTERN_ERROR);
        }

        saleProductService.insert(financialProductId, seckillActivityId, dto.getQuantity(), dto.getTotal());
        return ResultUtils.success();
    }

    @DeleteMapping("/product/{seckillActivityId}/{financialProductId}")
    @Operation(summary = "删除商品")
    public ResultVO<?> deleteProduct(@PathVariable String seckillActivityId, @PathVariable String financialProductId) {
        saleProductService.delete(seckillActivityId, financialProductId);
        return ResultUtils.success();
    }

    @PutMapping("/product/{seckillActivityId}/{financialProductId}")
    @Operation(summary = "更新商品")
    public ResultVO<?> updateProduct(@PathVariable String seckillActivityId, @PathVariable String financialProductId, @RequestBody SaleProductDto dto) {
        if(dto.getTotal() < 0 || dto.getQuantity() < 0 || dto.getQuantity() > dto.getTotal()) {
            throw new AppException(ResultCode.PATTERN_ERROR);
        }

        saleProductService.modify(financialProductId, seckillActivityId, dto.getQuantity(), dto.getTotal());
        return ResultUtils.success();
    }
}
