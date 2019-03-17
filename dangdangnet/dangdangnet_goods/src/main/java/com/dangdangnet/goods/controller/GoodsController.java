package com.dangdangnet.goods.controller;

import com.dangdangnet.common.dto.PageResult;
import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.goods.entity.Goods;
import com.dangdangnet.goods.entity.GoodsVO;
import com.dangdangnet.goods.entity.Label;
import com.dangdangnet.goods.service.GoodsService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 商品展示
 * @author pandora2333
 */
@RestController
@CrossOrigin
@RefreshScope
@RequestMapping("/show")
public class GoodsController {

    @Resource
    GoodsService goodsService;

    @GetMapping(value = "/search/{page}/{size}")
    public Result pageQuery(@PathVariable int page, @PathVariable int size, @RequestBody Label label){
        Page<Goods> pageData = goodsService.pageQuery(label, page, size);
        return new Result(StatusCode.OK, "查询结果",
                new PageResult<>(pageData.getTotalElements(), pageData.getContent()));
    }

    @GetMapping(value = "/{key}/{page}/{size}")//分页下标从0开始
    public Result findByKey(@PathVariable String key, @PathVariable int page, @PathVariable int size){
        Page pageData = goodsService.findByKey(key, page, size);
        System.out.println(pageData.getContent());
        if(pageData.getTotalElements()==0){
            pageData = goodsService.findByName(key,page,size);
            if(pageData.getTotalElements()!=0) goodsService.saveInES(pageData.getContent());
        }
        return new Result(StatusCode.OK, "查询结果", new PageResult(pageData.getTotalElements(), pageData.getContent()));
    }


}
