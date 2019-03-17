package com.dangdangnet.goods.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 购物车模型
 * @author pandora2333
 */
@Entity
@Table(name = "t_shoppingcar")
@Data
@Accessors(chain = true)
public class ShoppingCar {

    @Id
    private String id;
    private String user_id;//用户id
    private String addr;//配送地址
    private String phone;//用户电话
    private String user_name;//用户名
    private BigDecimal total;//总金额
}
