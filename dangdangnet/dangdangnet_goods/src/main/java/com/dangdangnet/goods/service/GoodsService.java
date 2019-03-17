package com.dangdangnet.goods.service;
import com.dangdangnet.goods.entity.Goods;
import com.dangdangnet.goods.entity.GoodsVO;
import com.dangdangnet.goods.entity.Label;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GoodsService {
    public Page<Goods> pageQuery(Label label, int page, int size);

    public void decreaseCount(String carid,String userid);

    public Page<GoodsVO> findByKey(String key, int page, int size);

    public Page<Goods> findByName(String key, int page, int size);

    public void saveInES(List<Goods> content);
}
