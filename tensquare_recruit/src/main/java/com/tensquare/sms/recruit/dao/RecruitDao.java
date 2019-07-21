package com.tensquare.sms.recruit.dao;

import com.tensquare.sms.recruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 数据访问接口
 * @author Administratort
 *
 */
public interface RecruitDao extends JpaRepository<Recruit,String>,JpaSpecificationExecutor<Recruit>{
    // findTop 查看前一条  findTop6 查看前六条 By根据 State状态 OrderByCreatetime 根据时间倒序显示
    public List<Recruit> findTop6ByStateOrderByCreatetimeDesc(String state); //where  state = ? order by createtime

    public List<Recruit> findTop6ByStateNotOrderByCreatetimeDesc(String state); //where state !=0
}
