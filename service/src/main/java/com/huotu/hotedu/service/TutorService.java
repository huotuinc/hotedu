package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Tutor;
import com.huotu.hotedu.repository.TutorRepository;
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
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    //返回所有导师信息
    public Page<Tutor> loadTutor(int n,int pagesize){
        return tutorRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页依据类型和关键字搜索
    public Page<Tutor> searchTutorType(int n,int pagesize,String keyword,String type){
        return  tutorRepository.findAll(new Specification<Tutor>() {
            @Override
            public Predicate toPredicate(Root<Tutor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.like(root.get(type).as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));

    }


    //分页依据全部搜索
    public Page<Tutor> searchTutorAll(int n,int pagesize,String keyword){
        return  tutorRepository.findAll(new Specification<Tutor>() {
            @Override
            public Predicate toPredicate(Root<Tutor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.or(cb.like(root.get("name").as(String.class), "%" + keyword + "%"),
                             cb.like(root.get("qualification").as(String.class),"%"+keyword+"%"),
                             cb.like(root.get("area").as(String.class),"%"+keyword+"%")
                );
            }
        },new PageRequest(n, pagesize));

    }

    //分页依据时间
    public Page<Tutor> searchTutorDate(int n,int pagesize,Date start,Date end){
        return  tutorRepository.findAll(new Specification<Tutor>() {
            @Override
            public Predicate toPredicate(Root<Tutor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (start==null&&end==null)
                    return null;
                return cb.between(root.get("lastUploadDate").as(Date.class),start,end);
            }
        },new PageRequest(n, pagesize));

    }



    //删除一个导师(包括他的照片)
    public void delTutor(Long id){
        try {
            URI uri=new URI(findOneById(id).getPictureUri());
            tutorRepository.delete(id);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    //增加一位导师
    public void addTutor(Tutor tutor){
        tutorRepository.save(tutor);
    }
    //修改一位导师信息
    public void modify(Tutor Tutor){
        tutorRepository.save(Tutor);

    }
    //查找一条考试消息
    public Tutor findOneById(Long id){
        return tutorRepository.findOne(id);
    }
}
