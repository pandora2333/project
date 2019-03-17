package com.dangdangnet.goods.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品搜索模型
 * @author pandora2333
 */
@Data
@Accessors(chain = true)
@Document(indexName = "dangdangnet_goods", type = "goods")
public class GoodsVO implements Serializable {
    @Id //ES标识主键
    private String id;
    @Field(analyzer="ik_max_word", searchAnalyzer="ik_max_word")
    private String name;//商品名称
    @Field(index = false)
    private BigDecimal sale;//售价
    @Field(index = false)
    private Integer count;//剩余量
    @Field(index = false)
    private String level1;//分类编号1
    @Field(index = false)
    private String level2;//分类编号2
    @Field(analyzer="ik_max_word", searchAnalyzer="ik_max_word")
    private String brand;//商标品牌
    @Field(index = false)
    private String show_pic;//商品图片
}
