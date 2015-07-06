package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.repository.QaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiliting 2015/6/25.
 *
 * @author shiliting
 */
@Service
public class QaService {

    @Autowired
    private QaRepository qaRepository;

    //返回所有常见问题
    public Page<Qa> loadQa(int n,int pagesize){
        return qaRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页
    public Page<Qa> searchQa(int n,int pagesize,String keyword){
        // SQL
        // 面向对象的SQL
        // select Qa from Qa where title like ?
        return  qaRepository.findAll(new Specification<Qa>() {
            @Override
            public Predicate toPredicate(Root<Qa> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.like(root.get("title").as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));

    }

    //删除一条常见问题消息
    public void delQa(Long id){
        qaRepository.delete(id);
    }

    //增加一条常见问题
    public void addQa(Qa Qa){
        qaRepository.save(Qa);
    }

    //修改一条常见问题
    public void modify(Qa Qa){
        qaRepository.save(Qa);

    }
    //查找一条常见问题
    public Qa findOneById(Long id){
        return qaRepository.findOne(id);
    }
}
