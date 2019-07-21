package com.tensquare.sms.base.dao;

import com.tensquare.sms.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/***
 * jpa的功能
 * 1.jpa具体实现功能包含几种
 *    直接实现JpaRepository的接口，可以通过jpa提供的方法对数据库进行操作
 *    jpa自定义接口中的@Query可以自定义查询语句，findBy动词自定义查询语句，可以进行
 *    分页查询，原生sql查询。
 * 2. jpaRepository定义了一组标准的接口，CrudRepository继承了Repository接口
 *    在jpaRepository定义了一组CRUD的操作方法，也就是只要我们的类实现jpaRepository接口就可以对模型对象进行CRUD操作
 *3.  jPaSpecificationExecutor接口提供了一组基于标准JPA Criteria查询相关的方法，也就是分页动态查询相关的操作
 *
 */
public interface LabelDao extends JpaRepository<Label,String>,JpaSpecificationExecutor<Label>{

}
