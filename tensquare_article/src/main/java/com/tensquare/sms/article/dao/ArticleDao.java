package com.tensquare.sms.article.dao;

import com.tensquare.sms.article.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *spring data 没有 mybatis查询更加强大
 *
 * spring data 和mybatis的最大区别：
 *  mybatis对于多表查询上要比spring data jpa要强的很多
 *  但对于其他的来说，spring data jpa要比mybatis要简单的多
 *
 * 服务器来自的压力在于：这个服务器的压力就在访问的一瞬间当中的内存中能不能吃的消，
 * 就要看他那一瞬间创建大量的对象  spring boot胡底层也还是spring mvc，spring mvc也就会创建一个对象
 *
 * streat2的controller每一人访问一次就会创建一个新的控制器，他呢就扛不住高并发
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    @Modifying //所有可能产生线程问题的都需要加上@Modifying    where id =?1 这个1的意思是参数的位置   nativequery=true值指定为原生sql
    @Query(value = "update tb_article set state =1 where id =?",nativeQuery = true)
    public void updateState(String id);

    @Modifying
    @Query(value = "update tb_article set thumbup =thumbup+1 where id =?",nativeQuery = true)
    public void addThumbup(String id);
}
