package com.ddnet.pojo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;
/**
 * 账户模型
 * @author pandora
 *
 */
@Data
@Accessors(chain=true)
public class Account {
	
	private Integer id;//主键标识ID
	private String name;//账户名
	private BigDecimal money;//余额
	private String addr;//地址ַ
	private String phone;//电话号码
	private Car use_car;//购物车
	private String pic;//用户头像
	private String pwd;//密码
}
