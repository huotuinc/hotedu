package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Enterprise;
import com.huotu.hotedu.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Created by shiliting 2015/7/13.
 *
 * @author shiliting
 */
@Service
public class EnterpriseService {

    @Autowired
    private EnterpriseRepository enterpriseRepository;

    //返回所有企业
    public Page<Enterprise> loadEnterprise(int n,int pagesize){
        return enterpriseRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页依据类型和关键字搜索
    public Page<Enterprise> searchEnterpriseType(int n,int pagesize,String keyword,String type){
        return  enterpriseRepository.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.like(root.get(type).as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));

    }


    //分页依据全部搜索
    public Page<Enterprise> searchEnterpriseAll(int n,int pagesize,String keyword){
        return  enterpriseRepository.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.or(cb.like(root.get("loginName").as(String.class), "%" + keyword + "%"),
                             cb.like(root.get("enterprise").as(String.class),"%"+keyword+"%")
                );
            }
        },new PageRequest(n, pagesize));

    }

    //分页依据时间
    public Page<Enterprise> searchEnterpriseDate(int n,int pagesize,Date start,Date end){
        return  enterpriseRepository.findAll(new Specification<Enterprise>() {
            @Override
            public Predicate toPredicate(Root<Enterprise> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (start==null&&end==null)
                    return null;
                return cb.between(root.get("lastUploadDate").as(Date.class),start,end);
            }
        },new PageRequest(n, pagesize));

    }



    //删除一个企业
    public void delEnterprise(Long id){
            enterpriseRepository.delete(id);
    }

    //增加一个企业
    public void addEnterprise(Enterprise enterprise){
        enterpriseRepository.save(enterprise);
    }
    //修改一个企业信息
    public void modify(Enterprise enterprise){
        enterpriseRepository.save(enterprise);
    }
    //查找一个企业信息
    public Enterprise findOneById(Long id){
        return enterpriseRepository.findOne(id);
    }
}
