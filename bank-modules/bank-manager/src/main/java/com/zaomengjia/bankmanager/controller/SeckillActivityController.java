package com.zaomengjia.bankmanager.controller;

import com.zaomengjia.bankmanager.service.SeckillActivityService;
import com.zaomengjia.common.message.Result;
import com.zaomengjia.common.pojo.SeckillActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckillActivity")
public class SeckillActivityController {
    @Autowired
    SeckillActivityService seckillActivityService;

    @GetMapping("/getAllSeckillActivity/{pageIndex}/{pageSize}")
    public Result getAllSeckillActivity(@PathVariable int pageIndex, @PathVariable int pageSize){
        try{
            return Result.succ(seckillActivityService.getSeckillActivity(pageIndex, pageSize));
        }catch (Exception e){
            return Result.fail(500, e.getMessage(), null);
        }
    }

    @GetMapping("/getSeckillActivityById/{id}")
    public Result getSeckillActivityById(@PathVariable long id){
        try{
            return Result.succ(seckillActivityService.getSeckillActivityById(id));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @GetMapping("/getSeckillActivityByName/{name}")
    public Result getSeckillActivityByName(@PathVariable String name){
        try{
            return Result.succ(seckillActivityService.getSeckillActivityByName(name));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),null);
        }
    }

    @PostMapping("/addSeckillActivity/{activity}")
    public Result addSeckillActivity(@RequestBody SeckillActivity activity){
        try{
            return Result.succ(seckillActivityService.addSeckillActivity(activity));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @DeleteMapping("/deleteSeckillActivity/{id}")
    public Result deleteSeckillActivity(@PathVariable long id){
        try{
            return Result.succ(seckillActivityService.deleteSeckillActivity(id));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }

    @PutMapping("/updateSeckillActivity/{activity}")
    public Result updateSeckillActivity(@RequestBody SeckillActivity activity){
        try{
            return Result.succ(seckillActivityService.updateSeckillActivity(activity));
        }catch (Exception e){
            return Result.fail(500,e.getMessage(),-1);
        }
    }
}
