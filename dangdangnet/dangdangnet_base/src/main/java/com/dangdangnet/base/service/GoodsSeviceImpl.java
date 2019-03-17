package com.dangdangnet.base.service;

import com.dangdangnet.base.dao.GoodsDao;
import com.dangdangnet.base.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GoodsSeviceImpl implements GoodsService {

    @Autowired
    GoodsDao goodsDao;
    @Override
    public void save(Goods goods) {
        goodsDao.save(goods);
    }

    @Override
    public Page<Goods> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return goodsDao.findAll(pageable);
    }

    @Override
    public Goods findById(String id) {
        Goods goods = null;
        Optional<Goods> temp = goodsDao.findById(id);
        if(temp.isPresent()) goods = temp.get();
        return goods;
    }

    @Override
    public void update(Goods goods) {
        save(goods);//全修改处理
    }

    @Override
    public void delete(String goodsId) {
        goodsDao.deleteById(goodsId);
    }
}
