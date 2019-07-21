package com.tensquare.sms.serarch.service;

import com.tensquare.sms.serarch.dao.AritcleDao;
import com.tensquare.sms.serarch.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

@Service  //此时不需要添加事物，因为是往索引库添加
@Transactional
public class ArticleService {

    @Autowired
    private AritcleDao aritcleDao;

    @Autowired
    private IdWorker idWorker;

    public void save(Article article){
        //article.setId(idWorker.nextId()+"");
        //es中会有id的默认值，可以省去idword
        aritcleDao.save(article);
    }


    //查询  根据es全文搜索引擎来搜索
    public Page<Article> findByKey(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        return aritcleDao.findByTitleOrContentLike(key,key,pageable);
    }
}
