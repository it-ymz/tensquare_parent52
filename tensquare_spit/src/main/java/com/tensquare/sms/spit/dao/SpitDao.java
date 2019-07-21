package com.tensquare.sms.spit.dao;

import com.tensquare.sms.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

//微服务的持久层使用的是接口    继承MongoRepository<Spit,String>
public interface SpitDao extends MongoRepository<Spit,String> {

    public Page<Spit> findByParentid(String parentid, Pageable pageable);
}
