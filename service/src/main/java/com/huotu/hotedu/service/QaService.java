package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.repository.QaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by shiliting 2015/6/25.
 *
 * @author shiliting
 */
@Service
public class QaService {

    @Autowired
    private QaRepository qaRepository;


    //分页
    public Page<Qa> searchQa(int pageNo,int pageSize,String keyword){
        return  qaRepository.findAll(new Specification<Qa>() {
            @Override
            public Predicate toPredicate(Root<Qa> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(keyword)||keyword==null) {
                    return null;
                }
                return cb.like(root.get("title").as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(pageNo, pageSize));

    }

    //删除一条常见问题消息
    public void delQa(Long id){
        qaRepository.delete(id);
    }

    //增加一条常见问题
    @Transactional
    public void addQa(Qa Qa){
        qaRepository.save(Qa);
    }

    //修改一条常见问题
    @Transactional
    public void modify(Qa Qa){
        qaRepository.save(Qa);
    }
    //查找一条常见问题
    public Qa findOneById(Long id){
        return qaRepository.findOne(id);
    }

    /**
     * 加载前台考试指南界面
     */
    public Page<Qa> loadPcQa(int n,int pageSize){
        return qaRepository.findAll(new PageRequest(n, pageSize,new Sort(Sort.Direction.DESC,"top","lastUploadDate")));
    }

}
