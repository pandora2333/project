package com.dangdangnet.goods.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品物流模型（多条物流封装到一个购物车中）
 * @author pandora2333
 */
@Entity
@Table(name = "t_flow")
@Data
@Accessors(chain = true)
public class Order {
    @Id
    private String id;//主键标识
    private String car_id;//购物车id
    private String user_id;//用户id
    private String goods_id;//商品id
    private String user_name;//用户名
    private Integer buy_count;//购买数量
}
