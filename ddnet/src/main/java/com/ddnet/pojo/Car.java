package com.ddnet.pojo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 购物车模型
 * @author pandora
 *
 */
@Data
@Accessors(chain=true)
public class Car {
	
	private String user_name;//对应用户
	private List<Order> orders;//需购买商品

}
