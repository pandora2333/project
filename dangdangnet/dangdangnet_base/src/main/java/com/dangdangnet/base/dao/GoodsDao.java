package com.dangdangnet.base.dao;

import com.dangdangnet.base.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface GoodsDao extends JpaRepository<Goods, String> , JpaSpecificationExecutor<Goods> {

}
