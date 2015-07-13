package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Banners;
import com.huotu.hotedu.repository.BannersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by shiliting 2015/6/25.
 *
 * @author shiliting
 */
@Service
public class BannersService {

    @Autowired
    private BannersRepository bannersRepository;

    //返回所有banners
    public Page<Banners> loadBanners(int n,int pagesize){return bannersRepository.findAll(new PageRequest(n,pagesize));}

    //分页
    public Page<Banners> searchBanners(int n,int pagesize,String keyword){
        // SQL
        // 面向对象的SQL
        // select Banners from Banners where title like ?
       return  bannersRepository.findAll(new Specification<Banners>() {
            @Override
            public Predicate toPredicate(Root<Banners> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.like(root.get("title").as(String.class), "%" + keyword + "%");
            }
        },new PageRequest(n, pagesize));
    }


    //分页依据时间
    public Page<Banners> searchBanners(int n,int pagesize,Date start,Date end){
        return  bannersRepository.findAll(new Specification<Banners>() {
            @Override
            public Predicate toPredicate(Root<Banners> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (start==null&&end==null)
                    return null;
                return cb.between(root.get("lastUploadDate").as(Date.class), start, end);
            }
        },new PageRequest(n, pagesize));

    }

    //删除一个banners
    public void delBanners(Long id){
        try {URI uri=new URI(findOneById(id).getContent());
            bannersRepository.delete(id);
        } catch (URISyntaxException e) {
            e.printStackTrace();}
    }



    //增加一条banners信息
    public void addBanners(Banners banners){bannersRepository.save(banners);}

    //修改一条banners信息
    public void modify(Banners banners){bannersRepository.save(banners);}

    //查找一条banners消息
    public Banners findOneById(Long id){return bannersRepository.findOne(id);}

}
