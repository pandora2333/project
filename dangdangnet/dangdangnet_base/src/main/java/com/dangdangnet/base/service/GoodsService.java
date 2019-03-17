package com.dangdangnet.base.service;

import com.dangdangnet.base.entity.Goods;
import org.springframework.data.domain.Page;

public interface GoodsService {
    public void save(Goods goods);

    public Page<Goods> findAll(int page, int size);

    public Goods findById(String id);

    public void update(Goods goods);

    public void delete(String goodsId);
}
