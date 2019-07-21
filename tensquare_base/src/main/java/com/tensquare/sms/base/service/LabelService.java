package com.tensquare.sms.base.service;

import com.tensquare.sms.base.dao.LabelDao;
import com.tensquare.sms.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service //创建一个实例  只有实例化以后才可以注入到spring容器中
public class LabelService {


    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll(){
        return labelDao.findAll();
    }

    public Label findById(String id){
        return labelDao.findById(id).get(); //findById在底层的返回类型是Optional一个容器，是JDK8的一个特性，
                                            //他有两种表现方式 一个是判断是否有值，然后在取值，另一种就是可以直接通过.get()方法获取到值
    }

    public void save(Label label){
        //idWorker.nextId()方法返回的是一串数字
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    public void update(Label label){
        labelDao.save(label);     //save()方法：即能做保存，又能做更新；对象里面包含id表示更新，对象里面没有id表示保存
                                 //save()方法分为两步操作，首先拿着id先去查，查到则是更新，没有查到就是保存
    }

    public void deleteById(String id){
        labelDao.deleteById(id);
    }

    /**
     * Specification中封装了查询方法
     *
     *   findAll(Specification specification)是查询的时候有条件的时候使用
     *   findAll(Pageable pageable) 当只有分页的时候使用该方法
     *   findAll(Specification specification.Pageable pageable) 是在有条件查询，并且还有分页的时候使用
     * @param label
     * @return
     */
    public List<Label> findSearch(Label label) {
        return labelDao.findAll(new Specification<Label>(){

            /**
             *
             * @param root  根对象，也就是要把条件封装到哪个对象中，where 类名 = label.getid  **(自我认为：root封装的就是查询条件在哪个对象中)
             * @param criteriaQuery  封装的都是查询关键字，比如 group by order by等
             * @param cb 用来封装条件对象的        ***(自我认为：criteriaBuilder中封装的就是各种的查询方法)
             * @return null 返回的类型是 Predicate 重写的这个方法返回值是null的话，表示不需要任何条件
             */
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //new 一个list集合，来存放所有的条件
                List<Predicate> list = new ArrayList<>();

                if(label.getLabelname()!=null && label.getLabelname()!=""){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");//这么一句话就相当于 where lablename like "%鲜明%"的一条sql语句
                    //cb里面封装的查询的方法  比如说 like模糊 ,requal相当于=的意思
                    list.add(predicate);
                }
                if(label.getState()!=null && label.getState()!=""){
                    Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState()); //这句话就相当于  where status = "1"
                    list.add(predicate);
                }
                //Predicate是一个数组，长度是不可变的，而predicate是返回查询条件，既然是模糊查询，
                //那么就不知道用户查询几个条件，那么就可以使用集合先来存储查询条件，因为集合是一个动态数组

                //new 一个数组作为最终返回值的条件
                Predicate[] parr = new Predicate[list.size()];
                //将list直接转为数组
                parr = list.toArray(parr);
                return cb.and(parr);  //当有多个条件的时候，那么就需要使用 and()方法  也有or()方法
                                        //  相当于sql中的 where labelname like  "%小明%" and state = "1"
            }
        });
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
        //封装一个分页对象   pageRequest这个分页工具默认是从0页开始的，而我们平时传值的时候是从第一页开始的，那么要想让这个分页工具显示第一页，那么就需要从0页开始
        Pageable pageable = PageRequest.of(page-1,size);
        return labelDao.findAll(new Specification<Label>() {  //Specification中封装了条件查询方法  它里面需要实现toPredicate方法
            /**
             * @param root          跟对象 root中封装的是查询条件的在哪个对象中
             * @param criteriaQuery //封装的查询关键字   order by
             * @param cb            //封装的是条件查询方法
             * @return
             */
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //创建一个list，用于存放所有的查询条件
                List<Predicate> list = new ArrayList<>();
                if (label.getLabelname() != null && label.getLabelname() != "") {
                    Predicate labelname = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%"); //相当于 where labelname like '%羡慕%'
                    list.add(labelname);
                }
                if (label.getState() != null && label.getState() != "") {
                    Predicate state = cb.equal(root.get("state").as(String.class), label.getState()); //相当于 where state = state
                    list.add(state);
                }

                //创建一个数组，作为最终返回值的条件
                Predicate[] pa = new Predicate[list.size()];
                pa = list.toArray(pa);
                return cb.and(pa);
            }
        },pageable);
    }

    //spring data jpql语句
}
