package com.dangdangnet.base.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品模型
 * @author pandora2333
 */
@Entity
@Table(name = "t_goods")
@Data
@Accessors(chain = true)
public class Goods implements Serializable {

    @Id
    private String id;
    private String name;//商品名称
    private BigDecimal sale;//售价
    private Integer count;//剩余量
    private String level1;//分类编号1
    private String level2;//分类编号2
    private String brand;//商标品牌
    private String show_pic;//商品图片

}
