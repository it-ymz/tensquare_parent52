package com.tensquare.sms.serarch.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

//es往索引库存储的基本单位就是文档

//数据库中的一行记录就是对应该类的一个对象
//那该类中的一个对象对应的就是es索引库的一个文档
@Document(indexName = "tensquare_article", type = "article")//既然是往索引库里面进行存储，那么就需要指定哪个索引 index="",有了索引，那么就需要指定往什么类型中存储
//elasticsearch索引与java连接是使用@Document来进行连接的，不过在连接的时候需要指定索引库和类型  索引库就是数据库，数据类型就是表
public class Article implements Serializable{

    @Id
    private String id;  //在es中显示什么，那么就只需要在该类中写该属性

    //是否索引：就是看该域能被搜索
    //是否分词：就表示搜索的时候是整体匹配还是单词匹配
    //是否存储：就是是否在页面上显示

    /**
     * 索引的意思就是 可以搜到它。比如说：从百度上搜索苹果，那么这个苹果就是被设置了索引，可以针对这个词进行搜索
     *
     * 是否分词：就是说一段话，是否需要分成几个词组进行搜索，如果说不分词的话，那么认为就是必须搜索的是整个一段话
     *
     * 是否存储：就是说是否在页面上进行显示。
     * 就比如说在百度上搜到一个成龙，那么就会出现关于成龙的图片，网址，标题，还包括简单的一个描述
     * 而当点击进去这篇文章，就会出现大篇的字幕。
     * 注意思考：此时，如果说有客户在输入框中输入的这篇文章的一段话，那么这篇文章是否应该显示。准确回答是肯定要搜索到。
     * 那么这就想到，页面上是不显示这一篇字幕的。这也就是说是否存储就是是否在页面上进行显示
     *
     * 只要在该类中写的属性，那么就会存储
     */
    //index索引  analyzer是存的时候使用ik分词器  searchAnalyzer  是在搜索的时候也是使用的ik分词器
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;

    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;
    private String state;

    public Article(String id, String title, String content, String state) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.state = state;
    }

    public Article() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
