package com.dangdangnet.base.controller;

import com.dangdangnet.base.entity.Goods;
import com.dangdangnet.base.service.GoodsService;
import com.dangdangnet.base.util.FastDFSClient;
import com.dangdangnet.common.dto.PageResult;
import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.common.dto.util.FileUtils;
import com.dangdangnet.common.dto.util.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;

/**
 * 商品增删改查
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin
@RefreshScope
@Slf4j
public class GoodsController {

    @Resource
    GoodsService goodsService;
    @Resource
    FastDFSClient fastDFSClient;
    @Autowired
    IdWorker idWorker;
    @Value("${pic.path}")
    String path;
    @PostMapping("save")
    public Result save(@RequestBody Goods goods){
        String pic_path = path+idWorker.nextId()+".png";
        String up_pic = pic_path;
        try {
            FileUtils.base64ToFile(pic_path,goods.getShow_pic());
            up_pic = fastDFSClient.uploadFile(FileUtils.fileToMultipart(pic_path));
            new File(pic_path).delete();
        } catch (Exception e) {
            log.info("error in base-save:{}", e);
            return new Result(StatusCode.ERROR, "图片解析失败", null);
        }
        goods.setShow_pic(up_pic);//数据库中保存图片网络地址
        goodsService.save(goods);
        return new Result(StatusCode.OK,"保存成功",null);
    }

    @GetMapping("findAll/{page}/{size}")
    public Result findAll(@PathVariable int page,@PathVariable int size){
        Page<Goods> goods = goodsService.findAll(page,size);
        return new Result(StatusCode.OK,"查询成功", new PageResult<Goods>(goods.getTotalElements(),goods.getContent()));
    }

    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id){
        Goods goods = goodsService.findById(id);
        return new Result(StatusCode.OK,"查询成功", goods);
    }

    @PutMapping(value = "/{goodsId}")
    public Result update(@PathVariable String goodsId, @RequestBody Goods goods){
        goods.setId(goodsId);
        goodsService.update(goods);
        return new Result(StatusCode.OK,"修改成功",null);
    }
    @DeleteMapping(value = "/{goodsId}")
    public Result delete(@PathVariable String goodsId){
        goodsService.delete(goodsId);
        return new Result(StatusCode.OK,"删除成功",null);
    }

}
