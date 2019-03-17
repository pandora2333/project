package com.ddnet.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
/**
 * 管理员模型
 * @author pandora
 *
 */
@Data
@Accessors(chain=true)
public class Admin {

	private Integer id;//主键标识ID
	private String name;//管理员名
	private String pwd;//密码
}
