package com.dangdangnet.base.service;

import com.dangdangnet.base.dao.LabelDao;
import com.dangdangnet.base.entity.Label;
import com.dangdangnet.common.dto.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Label label) {
        labelDao.save(label);
    }

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    public void update(Label label) {
        labelDao.save(label);
    }

    public void delete(String labelId) {
        labelDao.deleteById(labelId);
    }

    public List<Label> findSearchLike(Label label){
        return labelDao.findAll(new Specification<Label>(){
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if(label.getLevel2_name()!=null && !"".equals(label.getLevel2_name())){
                    Predicate predicate = cb.like(root.get("level2_name").as(String.class), "%" + label.getLevel2_name() + "%");
                    list.add(predicate);
                }
                Predicate[] parr = new Predicate[list.size()];
                //把集合中的属性复制到数组中
                parr = list.toArray(parr);
                return cb.and(parr);
            }
        });
    }

    public List<Label> findSearch(Label label){
        return labelDao.findAll(new Specification<Label>(){
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if(label.getLevel1_name()!=null && !"".equals(label.getLevel1_name())){
                    Predicate predicate = cb.like(root.get("level1_name").as(String.class),label.getLevel1_name());
                    list.add(predicate);
                }
                Predicate[] parr = new Predicate[list.size()];
                //把集合中的属性复制到数组中
                parr = list.toArray(parr);
                return cb.and(parr);
            }
        });
    }

}
