package com.tensquare.sms.spit.service;

import com.tensquare.sms.spit.dao.SpitDao;
import com.tensquare.sms.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
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

    //发布吐槽
    public void save(Spit spit){
       spit.set_id(idWorker.nextId()+"");
       spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//会服数
        spit.setState("1");//状态
        //如果当前添加的吐槽，有父节点，那么父节点的吐槽回复书要加1

        //判断当前用户的父id是否为空，当不等于空的话，那么认为当前用户在和别人做评论，也就是别人的评论量加1
        if(spit.getParentid()!=null && spit.getParentid()!=""){
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }
       spitDao.save(spit);
    }

    public void update(Spit spit){
        spitDao.save(spit);
    }

    public void delete(String id){
        spitDao.deleteById(id);
    }

    public Page<Spit> findByParentid(String parentid,int page,int size){
        //spring data 中分页使用的pageable这个类，在需要填写页数的时候，需要使用pageRequest.of()来进行填写分页，然后将分页传入dao层进行分页
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByParentid(parentid,pageable);
    }

    public void thumbup(String spitId){
        //方式一：效率存在问题  会与数据库交互两次
        Spit spit = spitDao.findById(spitId).get();
        spit.setThumbup((spit.getThumbup()==null ? 0:spit.getThumbup())+1);
        spitDao.save(spit);

        //方式二：使用原生的MongoDB命令来实现自增，db.spit.update({"_id":1},{$inc:{"thumbup":NumberInt(1)}})
        //Query query = new Query();
        //query.addCriteria(Criteria.where("_id").is("2")); //查询条件 addCriteria(Criteria.where("_id").is("2"))
        //Update update = new Update();
        //update.inc("thumbup",1);
        //mongoTemplate.updateFirst(query,update,"spit");
    }
}
