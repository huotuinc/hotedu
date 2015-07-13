package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Link;
import com.huotu.hotedu.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 *
 * @author jiashubing
 */
@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    //返回所有友情链接
    public Page<Link> loadLink(int n,int pagesize){
        return linkRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页
    public Page<Link> searchLink(int n,int pagesize,String keyword){
        // SQL
        // 面向对象的SQL
        return  linkRepository.findAll(new Specification<Link>() {
            @Override
            public Predicate toPredicate(Root<Link> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.or(cb.like(root.get("title").as(String.class),"%"+keyword+"%"),cb.like(root.get("url").as(String.class),"%"+keyword+"%"));
               // return cb.like(root.get("title").as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));

    }

    //删除一条友情链接消息
    public void delLink(Long id){
        linkRepository.delete(id);
    }

    //增加一条友情链接信息
    public void addLink(Link link){
        linkRepository.save(link);
    }

    //修改一条友情链接信息
    public void modify(Link link){linkRepository.save(link);
    }

    //查找一条友情链接消息
    public Link findOneById(Long id){
        return linkRepository.findOne(id);
    }
}
