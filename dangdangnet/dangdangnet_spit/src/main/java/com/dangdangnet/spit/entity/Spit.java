package com.dangdangnet.spit.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户吐槽信息
 */
@Data
@Accessors(chain = true)
public class Spit implements Serializable {
    @Id
    private String _id;
    private String content;
    private Date publishtime;
    private String userid;
    private String nickname;
    private Integer comment;
    private String parentid;
}
