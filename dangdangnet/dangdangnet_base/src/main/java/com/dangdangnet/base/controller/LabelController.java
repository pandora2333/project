package com.dangdangnet.base.controller;

import com.dangdangnet.base.entity.Label;
import com.dangdangnet.base.service.LabelService;
import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.common.dto.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类：一级，二级，
 * @author pandora
 */
@RestController
@RequestMapping("/label")
@CrossOrigin
@RefreshScope
public class LabelController {

    @Resource
    private LabelService labelService;

    @PostMapping("save")
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(StatusCode.OK,"保存成功",null);
    }

    @GetMapping("findAll")
    public Result findAll(){
        List<Label> list = labelService.findAll();
        return new Result(StatusCode.OK,"查询成功", list);
    }

    @GetMapping(value = "/{labelId}")//一级id映射一级分类
    public Result findById(@PathVariable("labelId") String id){
        Label label = labelService.findById(id);
        return new Result(StatusCode.OK,"查询成功", label);
    }

    @PutMapping(value = "/{labelId}")
    public Result update(@PathVariable Integer labelId, @RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return new Result(StatusCode.OK,"修改成功",null);
    }
    @DeleteMapping(value = "/{labelId}")
    public Result delete(@PathVariable String labelId){
        labelService.delete(labelId);
        return new Result(StatusCode.OK,"删除成功",null);
    }

    @GetMapping(value = "/search")
    public Result findSearch(@RequestBody Label label){
        List<Label> list = labelService.findSearch(label);
        return new Result(StatusCode.OK,"查询成功", list);
    }

    @GetMapping(value = "/searchLike")
    public Result findSearchLike(@RequestBody Label label){
        List<Label> list = labelService.findSearchLike(label);
        return new Result(StatusCode.OK,"模糊查询成功", list);
    }
}
