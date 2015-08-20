package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Wanted;
import com.huotu.hotedu.repository.WantedRepository;
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
import java.util.Date;

/**
 * Created by cwb on 2015/7/10.
 */
@Service
public class WantedService {

    @Autowired
    WantedRepository wantedRepository;

    //加载招聘信息列表
    public Page<Wanted> loadVideo(int n,int pagesize){
        return wantedRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页
    public Page<Wanted> searchWanted(int n,int pagesize,String keyword) {
        return wantedRepository.findAll(new Specification<Wanted>() {
            @Override
            public Predicate toPredicate(Root<Wanted> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0) {
                    return null;
                }
                return cb.or(cb.like(root.get("wantedName").as(String.class), "%" + keyword + "%"));
            }
        },new PageRequest(n, pagesize));
    }


    public Page<Wanted> searchWantedById(int n,int pageSize,long id) {
        return wantedRepository.findAll(new Specification<Wanted>() {
            @Override
            public Predicate toPredicate(Root<Wanted> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (id==0) {
                    return null;
                }
                return cb.or(cb.equal(root.get("wantedName").as(Long.class), id));
            }
        },new PageRequest(n, pageSize));
    }

    public void delWanted(Long id){
        wantedRepository.delete(id);
    }

    @Transactional
    public void addWanted(Wanted video){
        wantedRepository.save(video);
    }


    public Wanted findOneById(Long id) {
        return wantedRepository.findOne(id);
    }


    //分页依据类型和关键字搜索
    public Page<Wanted> searchWantedType(int n,int pagesize,String keyword,String type){
        return  wantedRepository.findAll(new Specification<Wanted>() {
            @Override
            public Predicate toPredicate(Root<Wanted> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length() == 0)
                    return null;
                return cb.like(root.get(type).as(String.class), "%" + keyword + "%");
            }
        }, new PageRequest(n, pagesize));
    }

    //分页依据全部搜索
    public Page<Wanted> searchWantedAll(int n,int pagesize,String keyword){
        return  wantedRepository.findAll(new Specification<Wanted>() {
            @Override
            public Predicate toPredicate(Root<Wanted> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length() == 0)
                    return null;
                return cb.or(cb.like(root.get("loginName").as(String.class), "%" + keyword + "%"),
                        cb.like(root.get("enterprise").as(String.class), "%" + keyword + "%")
                );
            }
        }, new PageRequest(n, pagesize));
    }


    //分页依据时间
    public Page<Wanted> searchWantedDate(int n,int pagesize,Date start,Date end){
        return  wantedRepository.findAll(new Specification<Wanted>() {
            @Override
            public Predicate toPredicate(Root<Wanted> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (start == null && end == null)
                    return null;
                return cb.between(root.get("lastUploadDate").as(Date.class), start, end);
            }
        }, new PageRequest(n, pagesize));
    }

    //返回所有招聘信息
    public Page<Wanted> loadWanted(int n,int pagesize){
        return wantedRepository.findAll(new PageRequest(n,pagesize));
    }
}
