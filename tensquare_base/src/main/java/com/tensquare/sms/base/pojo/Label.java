package com.tensquare.sms.base.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * jpa是一个orm框架，
 * orm的意思是对象关系映射，关系型数据库和对象之间一一映射
 * 对象关系映射：关系型数据库即是mysql数据库，对象就是java中具体的类，他们的字段要一一对应
 * 所以jpa中的字段也需要和数据库表中的字段一一对应
 *
 *
 * jpa基本介绍：
 * 1.与hibernate的关系：jpa是标准，实际上jpa只是定义了接口，实现都是hibernate在做
 * 2.jpa存在的目的：让spring实现持久层操作，spring本来对第三方框架的整合性特别好
 * 3.jpa提供的功能：
 * hibernate，mybatis，jpa 进行对比
 * 一般的框架都会有一个对象操作DB(数据库)，比如：hibernate的session，mybatis的sqlsession，jpa的entityManager
 * 一般的orm框架只能提供 crud操作，但是jpa可以提供逻辑处理。对于不同的功能有着不同的实现，实现这些
 * 功能的核心代码就叫业务逻辑，
 */

//spring data jpa中需要在类上面添加Entity
@Entity  //必须有  表示类和表建立映射关系
@Table(name = "tb_label") //非必须  默认表名和类名相同 驼峰命名转_命名
public class Label implements Serializable{ //做分布式开发一定要实现序列化  只有加了序列化的对象才能在不同的平台之间使用IO流进行传输
    @Id  //只标明是唯一的注解
    //@GeneratedValue(GenerationType.AUTO)自增长id  在hibernate中做id自增长
    //@Transient  表示不和数据库建立关联
    private String id;
    private String labelname; //标签名称
    private String state;    //状态
    private Long count;      //使用数量
    private Long fans;       //关注数
    private String recommend;//是否推荐

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getFans() {
        return fans;
    }

    public void setFans(Long fans) {
        this.fans = fans;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public Label(String id, String labelname, String state, Long count, Long fans, String recommend) {
        this.id = id;
        this.labelname = labelname;
        this.state = state;
        this.count = count;
        this.fans = fans;
        this.recommend = recommend;
    }

    public Label() {
    }
}
