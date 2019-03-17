package com.dangdangnet.goods.dao;

import com.dangdangnet.goods.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlowDao extends JpaRepository<Order,String>, JpaSpecificationExecutor<Order> {

    @Modifying
    @Query(value = "delete from t_flow where car_id = :carid and user_id = :userid",nativeQuery = true)
    public void deleteByCar_idAndUser_id(@Param("carid") String carid,@Param("userid") String userid);
    @Query(value = "select * from t_flow where car_id = :carid and user_id = :userid",nativeQuery = true)
    public List<Order> findAllByCar_idAndUser_id(@Param("carid") String carid,@Param("userid") String userid);
    @Modifying
    @Query(value = "update t_flow set buy_count = :buy_count where id = :id",nativeQuery = true)
    public void updateById(@Param("buy_count") int buy_count, @Param("id")String id);
}
