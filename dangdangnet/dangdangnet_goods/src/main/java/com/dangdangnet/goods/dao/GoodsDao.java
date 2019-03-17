package com.dangdangnet.goods.dao;

import com.dangdangnet.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoodsDao extends JpaRepository<Goods,String> , JpaSpecificationExecutor<Goods> {

    @Modifying
    @Query(value =  "update t_goods set count = :count where id = :goods_id for update",nativeQuery = true)
    public void saveLocked(@Param("count")int count,@Param("goods_id") String goods_id);//加锁修改

    public Page<Goods> findByNameOrBrandLike(String name, String brand, Pageable pageable);
}
