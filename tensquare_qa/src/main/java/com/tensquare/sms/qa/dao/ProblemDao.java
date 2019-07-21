package com.tensquare.sms.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.sms.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    //默认的是jpql查询语句  面向的是对象  所以不能出现表名  当参数nativeQuery为true的时候，那么就说明写的是sql
    //注意：在使用分页查看的时候，那么返回值就是Page
    //查看最新论文
    @Query(value = "select * from tb_problem LEFT JOIN tb_pl on id = problemid and labelid = ? order by replytime desc",nativeQuery = true)
    public Page<Problem> newlist(String labelid, Pageable pageable);

    //查看最热论文
    @Query(value = "select * from tb_problem LEFT JOIN tb_pl on id = problemid and labelid = ? order by reply desc",nativeQuery = true)
    public Page<Problem> hotlist(String labelid, Pageable pageable);

    //查看
    @Query(value ="select * from tb_problem LEFT JOIN tb_pl on id = problemid and labelid = ? and reply = 0 order by createtime desc" ,nativeQuery = true)
    public Page<Problem> waitlist(String labelid, Pageable pageable);
}
