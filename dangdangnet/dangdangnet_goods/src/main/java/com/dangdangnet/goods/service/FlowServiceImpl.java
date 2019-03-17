package com.dangdangnet.goods.service;

import com.dangdangnet.common.dto.util.IdWorker;
import com.dangdangnet.goods.dao.CarDao;
import com.dangdangnet.goods.dao.FlowDao;
import com.dangdangnet.goods.dao.GoodsDao;
import com.dangdangnet.goods.entity.Goods;
import com.dangdangnet.goods.entity.Order;
import com.dangdangnet.goods.entity.ShoppingCar;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FlowServiceImpl implements  FlowService {
    @Resource
    FlowDao flowDao;
    @Resource
    CarDao carDao;
    @Resource
    GoodsDao goodsDao;
    @Resource
    IdWorker idWorker;

    @Override
    public void insertAll(Order[] orders, String username, String userid) {
        ShoppingCar shoppingCar = carDao.findByUser_id(userid);
        if(shoppingCar == null){
            shoppingCar = new ShoppingCar();
            shoppingCar.setId(idWorker.nextId()+"");
            shoppingCar.setUser_id(userid);
            shoppingCar.setUser_name(username);
        }
        BigDecimal money = new BigDecimal("0");
        for (Order order:orders) {
            order.setUser_id(userid);
            order.setUser_name(username);
            order.setCar_id(shoppingCar.getId());
            Optional<Order> optional = flowDao.findOne(new Specification<Order>(){
                @Override
                public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> list = new ArrayList<>();
                    if(StringUtils.isNotBlank(username)){
                        Predicate predicate = cb.like(root.get("user_name").as(String.class),username);
                        list.add(predicate);
                    }
                    if(StringUtils.isNotBlank(order.getGoods_id())){
                        Predicate predicate = cb.like(root.get("goods_id").as(String.class),order.getGoods_id());
                        list.add(predicate);
                    }
                    Predicate[] parr = new Predicate[list.size()];
                    //把集合中的属性复制到数组中
                    parr = list.toArray(parr);
                    return cb.and(parr);
                }
            });
            Order temp = null;
            if(optional.isPresent())temp = optional.get();
            if(temp!=null) order.setBuy_count(temp.getBuy_count()+order.getBuy_count());
            else order.setId(idWorker.nextId()+"");
            Optional<Goods> search = goodsDao.findById(order.getGoods_id());
            Goods goods = null;
            if(search.isPresent()) goods = search.get();
            if(goods!=null) money.add(goods.getSale().multiply(new BigDecimal(""+order.getBuy_count())));
            flowDao.save(order);
        }
        shoppingCar.setTotal(money);
        carDao.save(shoppingCar);
    }

    @Override
    public void updateGoods(Order[] orders) {
        for(Order order:orders)
            flowDao.updateById(order.getBuy_count(),order.getId());
    }

    @Override
    public void delGoods(Order[] orders) {
        for(Order order:orders)
            flowDao.delete(order);
    }
}
