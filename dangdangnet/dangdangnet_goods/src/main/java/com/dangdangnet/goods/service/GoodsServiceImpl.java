package com.dangdangnet.goods.service;
import com.dangdangnet.goods.dao.FlowDao;
import com.dangdangnet.goods.dao.GoodsDao;
import com.dangdangnet.goods.dao.SearchDao;
import com.dangdangnet.goods.entity.Goods;
import com.dangdangnet.goods.entity.GoodsVO;
import com.dangdangnet.goods.entity.Label;
import com.dangdangnet.goods.entity.Order;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GoodsServiceImpl implements  GoodsService {

    @Resource
    FlowDao flowDao;
    @Resource
    GoodsDao goodsDao;
    @Autowired
    SearchDao searchDao;
    public Page<Goods> pageQuery(Label label, int page, int size){
        //封装了一个分页对象，在springdatajpa中想要实现分页，直接传一个分页对象即可
        Pageable pageable = PageRequest.of(page-1, size);
        return goodsDao.findAll(new Specification<Goods>(){
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if(StringUtils.isNotBlank(label.getLevel2_name())){
                    Predicate predicate = cb.like(root.get("level2_name").as(String.class),  label.getLevel2_name());
                    list.add(predicate);
                }
                Predicate[] parr = new Predicate[list.size()];
                //把集合中的属性复制到数组中
                parr = list.toArray(parr);
                return cb.and(parr);
            }
        }, pageable);
    }

    @Override
    public void decreaseCount(String carid,String userid) {
        List<Order> orderList = flowDao.findAllByCar_idAndUser_id(carid,userid);
        if(orderList == null || orderList.isEmpty()) return;
        for(Order order:orderList){
            boolean lock = false;
            Optional<Goods> search = goodsDao.findById(order.getGoods_id());
            Goods goods = null;
            if(search.isPresent()) goods = search.get();
            int free_count = goods.getCount()-order.getBuy_count();//商品剩余数量
            if(free_count < 0) return;//不够购买，支付失败
            if(free_count <= 5) lock  = true;//并发修改数过多时，小于阈值情况下需要加锁修改，这里使用悲观锁
            goods.setCount(free_count);//修改商品数量
            if(!lock)
                goodsDao.save(goods);
            else
                goodsDao.saveLocked(goods.getCount(),goods.getId());
        }
    }

    @Override
    public Page<GoodsVO> findByKey(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return searchDao.findByNameOrBrandLike(key,key,pageable);//findByTitleOrContentLike
    }

    @Override
    public Page<Goods> findByName(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return goodsDao.findByNameOrBrandLike(key,key,pageable);
    }

    @Override
    public void saveInES(List<Goods> content) {
        for(Goods good:content) {
            GoodsVO goodsVO = new GoodsVO();
            BeanUtils.copyProperties(good,goodsVO);
            searchDao.save(goodsVO);
        }
    }
}
