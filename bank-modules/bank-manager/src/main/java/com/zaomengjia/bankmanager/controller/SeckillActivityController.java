package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.SeckillActivityService;
import com.zaomengjia.common.constant.ResultCode;
import com.zaomengjia.common.pojo.SeckillActivity;
import com.zaomengjia.common.utils.ResultUtils;
import com.zaomengjia.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckillActivity")
public class SeckillActivityController {
    @Autowired
    SeckillActivityService seckillActivityService;

    @GetMapping("/getAllSeckillActivity/{pageIndex}/{pageSize}")
    public ResultVO<?> getAllSeckillActivity(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return ResultUtils.success(seckillActivityService.getSeckillActivity(pageIndex, pageSize));
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @GetMapping("/getSeckillActivityById/{id}")
    public ResultVO<?> getSeckillActivityById(@PathVariable long id){
        try{
            return ResultUtils.success(seckillActivityService.getSeckillActivityById(id));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/getSeckillActivityByName/{name}")
    public ResultVO<?> getSeckillActivityByName(@PathVariable String name){
        try{
            return ResultUtils.success(seckillActivityService.getSeckillActivityByName(name));
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping("/addSeckillActivity/{activity}")
    public ResultVO<?> addSeckillActivity(@RequestBody SeckillActivity activity){
        try{
            seckillActivityService.addSeckillActivity(activity);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/deleteSeckillActivity/{id}")
    public ResultVO<?> deleteSeckillActivity(@PathVariable long id){
        try{
            seckillActivityService.deleteSeckillActivity(id);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping("/updateSeckillActivity/{activity}")
    public ResultVO<?> updateSeckillActivity(@RequestBody SeckillActivity activity){
        try{
            seckillActivityService.updateSeckillActivity(activity);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(ResultCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
