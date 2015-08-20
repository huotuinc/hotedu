package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.repository.ClassTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Service
public class ClassTeamService  {

    @Autowired
    private ClassTeamRepository classTeamRepository;
    //分页
    public Page<ClassTeam> searchClassTeam(Agent agent,int pageNo,int pageSize,String keyword){
        return  classTeamRepository.findAll(new Specification<ClassTeam>() {
            @Override
            public Predicate toPredicate(Root<ClassTeam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(keyword)||keyword==null){
                    return cb.equal(root.get("agent").as(Agent.class),agent);
                }else{
                    return cb.or(
                            cb.like(root.get("className").as(String.class), "%" + keyword + "%"),
                            cb.like(root.get("exam").get("examAddress").as(String.class), "%" + keyword + "%"),
                            cb.like(root.get("agent").get("area").as(String.class), "%" + keyword + "%")
                    );
                }
            }
        },new PageRequest(pageNo, pageSize));

    }


    //删除一个classteam
    public void delClassTeam(Long id){classTeamRepository.delete(id);}

    //增加一个classteam
    @Transactional
    public void addClassTeam(ClassTeam classteam){classTeamRepository.save(classteam);}

    //修改一个classteam
    @Transactional
    public void modify(ClassTeam classTeam){classTeamRepository.save(classTeam);}

    //查找一个classteam
    public ClassTeam findOneById(Long id){return classTeamRepository.findOne(id);}


}
