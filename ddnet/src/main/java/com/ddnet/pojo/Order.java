package com.ddnet.pojo;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 订单，映射商品及对应的数量的关系
 * @author pandora
 *
 */
@Data
@Accessors(chain=true)
public class Order {
	
	private Integer goods_id;//商品id
	private Integer buy_count;//购买数量

}
