package com.dangdangnet.spit.service;

import com.dangdangnet.common.dto.util.IdWorker;
import com.dangdangnet.spit.dao.SpitDao;
import com.dangdangnet.spit.entity.Spit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdWorker idWorker;

    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        //初始化数据完善
        spit.setPublishtime(new Date());//发布日期
        spit.setComment(0);//回复数
        //判断当前吐槽是否有父节点
        if(StringUtils.isNotBlank(spit.getParentid())){
            //给父节点吐槽的回复数加一
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "spit");
        }
        spitDao.save(spit);
    }

    public void update(Spit spit){
        spitDao.save(spit);
    }

    public void deleteById(String id){
        spitDao.deleteById(id);
    }


    public Page<Spit> pageQuery(String parentid, int page, int size){
        Pageable pageable = PageRequest.of(page-1, size);
        return spitDao.findByParentid(parentid, pageable);
    }

    //db.spit.update({_id:"2"},{$inc:{visits:NumberInt(1)}} )
}
