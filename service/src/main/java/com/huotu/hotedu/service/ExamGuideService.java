package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.repository.ExamGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
public class ExamGuideService {

    @Autowired
    private ExamGuideRepository examGuideRepository;

    //分页
    public Page<ExamGuide> searchExamGuide(int n,int pagesize,String keyword){
       return  examGuideRepository.findAll(new Specification<ExamGuide>() {
            @Override
            public Predicate toPredicate(Root<ExamGuide> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(keyword)||keyword==null) {
                    return null;
                }
                return cb.like(root.get("title").as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));

    }

    //删除一条考试指南消息
    public void delExamGuide(Long id){
        examGuideRepository.delete(id);
    }

    //增加一条考试信息
    public void addExamGuide(ExamGuide examGuide){
        examGuideRepository.save(examGuide);
    }

    //修改一条考试信息
    public void modify(ExamGuide examGuide){
        examGuideRepository.save(examGuide);

    }
    //查找一条考试消息
    public ExamGuide findOneById(Long id){
        return examGuideRepository.findOne(id);
    }

}
