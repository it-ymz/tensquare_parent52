package com.tensquare.sms.serarch.dao;

import com.tensquare.sms.serarch.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

//在使用es的时候dao层是继承的是elasticsearchRepository<>
public interface AritcleDao extends ElasticsearchRepository<Article,String>{

    //在es全文搜索的时候需要把所有可能有关键字的地方都要查
    public Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);
}
