package com.dangdangnet.goods.dao;
import com.dangdangnet.goods.entity.GoodsVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchDao extends ElasticsearchRepository<GoodsVO, String> {
    public Page<GoodsVO> findByNameOrBrandLike(String name, String brand, Pageable pageable);
}
