package com.dangdangnet.base.entity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 通过一级分类id查找二级分类id
 */
@Entity
@Table(name = "t_level2_level1")
@Data
@Accessors(chain = true)
public class Label {
    @Id
    private Integer id;
    private String level2_name;
    private String level1_name;

}
