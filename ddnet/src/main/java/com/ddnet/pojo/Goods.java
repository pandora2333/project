package com.ddnet.pojo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品模型
 * @author pandora
 *
 */
@Data
@Accessors(chain=true)
public class Goods {

	private Integer id;//主键标识ID
	private String goods_name;//商品名
	private String brand;//商标
	private Integer count;//数量
	private BigDecimal sale_money;//售价
	private String pic;//商品图片
}
