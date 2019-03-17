package com.dangdangnet.goods.dao;

import com.dangdangnet.goods.entity.ShoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarDao extends JpaSpecificationExecutor<ShoppingCar>, JpaRepository<ShoppingCar,String> {

    @Query(value = "select * from t_shoppingcar where user_id = :userid",nativeQuery = true)
    public ShoppingCar findByUser_id(@Param("userid") String userid);
    @Modifying
    @Query(value = "delete from t_shoppingcar where id =:id",nativeQuery = true)
    public void deleteById(@Param("id") String id);
}
