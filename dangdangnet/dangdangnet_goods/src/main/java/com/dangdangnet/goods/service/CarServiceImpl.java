package com.dangdangnet.goods.service;

import com.dangdangnet.goods.client.UserClient;
import com.dangdangnet.goods.dao.CarDao;
import com.dangdangnet.goods.dao.FlowDao;
import com.dangdangnet.goods.entity.ShoppingCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class CarServiceImpl implements CarService {
    @Resource
    CarDao carDao;
    @Resource
    FlowDao flowDao;
    @Autowired
    UserClient userClient;
    @Autowired
    GoodsService goodsService;
    @Override
    public boolean payMoney(String user, String id) {//购物车结算,拆分购物清单并清空购物车
        ShoppingCar shoppingCar = carDao.findByUser_id(id);
        if(shoppingCar==null) return false;
        //账户结算
        boolean flag = true;
        try {
            userClient.updateUser(id,""+shoppingCar.getTotal().floatValue());
        }catch (Exception e){
            flag = false;
        }
        if(flag) {
            //商品数量衰减
            goodsService.decreaseCount(shoppingCar.getId(),id);
            //清空订单
            flowDao.deleteByCar_idAndUser_id(shoppingCar.getId(), id);
            //删除购物车
            carDao.deleteById(shoppingCar.getId());
            return true;
        }
        return false;
    }
}
