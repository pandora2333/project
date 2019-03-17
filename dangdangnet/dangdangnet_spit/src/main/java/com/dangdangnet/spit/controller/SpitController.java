package com.dangdangnet.spit.controller;

import com.dangdangnet.common.dto.PageResult;
import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.spit.entity.Spit;
import com.dangdangnet.spit.service.SpitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {
    @Autowired
    private SpitService spitService;

    @GetMapping("find")
    public Result findAll(){
        return new Result(StatusCode.OK, "查询成功", spitService.findAll());
    }

    @GetMapping(value = "/{spitId}")
    public Result findById(@PathVariable String spitId){
        return new Result(StatusCode.OK, "查询成功", spitService.findById(spitId));
    }

    @PostMapping("save")
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(StatusCode.OK, "添加成功",null);
    }

    @PutMapping(value = "/{spitId}")
    public Result update(@PathVariable String spitId, @RequestBody Spit spit){
        spit.set_id(spitId);
        spitService.update(spit);
        return new Result(StatusCode.OK, "修改成功",null);
    }


    @DeleteMapping(value = "/{spitId}")
    public Result delete(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(StatusCode.OK, "删除成功",null);
    }

    @GetMapping(value = "/comment/{parentid}/{page}/{size}")
    public Result comment(@PathVariable String parentid, @PathVariable int page, @PathVariable int size){
        Page<Spit> pageData = spitService.pageQuery(parentid, page, size);
        return new Result(StatusCode.OK, "查询成功", new PageResult<Spit>(pageData.getTotalElements(), pageData.getContent()));
    }
}
