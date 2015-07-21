package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Exam;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.repository.ClassTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Service
public class ClassTeamService  {

    @Autowired
    private ClassTeamRepository classTeamRepository;

    //返回所有班级管理
    public Page<ClassTeam> loadClassTeam(int n,int pagesize){
        return classTeamRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页
    public Page<ClassTeam> searchClassTeam(int n,int pagesize,String keyword){
        // SQL
        // 面向对象的SQL
        // select ClassTeam from ClassTeam where title like ?
        return  classTeamRepository.findAll(new Specification<ClassTeam>() {
            @Override
            public Predicate toPredicate(Root<ClassTeam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.like(root.get("title").as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));

    }


    //删除一个classteam
    public void delClassTeam(Long id){classTeamRepository.delete(id);}

    //增加一个classteam
    public void addClassTeam(ClassTeam classteam){classTeamRepository.save(classteam);}

    //修改一个classteam
    public void modify(ClassTeam classTeam){classTeamRepository.save(classTeam);}

    //查找一个classteam
    public ClassTeam findOneById(Long id){return classTeamRepository.findOne(id);}


}
