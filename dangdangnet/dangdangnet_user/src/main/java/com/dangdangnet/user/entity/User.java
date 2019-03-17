package com.dangdangnet.user.entity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 帐户信息
 * @author  pandora2333
 */
@Data
@Accessors(chain = true)
@Entity
@Table(name="t_account")
public class User implements Serializable {
    @Id
    private String id;//ID标识
    @Size(max = 64,min = 2)
    private String username;//账户名

    private BigDecimal use_money;//账户余额
    private Integer frozeen;//是否冻结使用
    @NotNull
    private String password;//账户密码
    private String avatar;//头像
    private String email;//E-Mail
    private String nickname;//昵称
    private String message;//个性留言
    private String address;//用户地址
    private Integer sex;//性别
    private String phone;//手机号
    private java.util.Date date;//注册日期
}
