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


    /**
     * 返回未被禁用的导师信息
     * @param n          显示的页数
     * @param pageSize   每页显示记录的条数
     * @return           导师的集合
     */
    public Page<Tutor> loadTutor(int n,int pageSize) {
        return tutorRepository.findAll(new Specification<Tutor>() {
            @Override
            public Predicate toPredicate(Root<Tutor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.isTrue(root.get("enabled").as(Boolean.class));
            }
        }, new PageRequest(n, pageSize));
    }


    /**
     * 返回根据类型和关键字搜索之后的导师信息
     * @param n         显示的页数
     * @param pageSize  每页显示记录的条数
     * @param keyword   检索的关键字
     * @param type      检索的类型
     * @return          返回的导师集合
     */
    public Page<Tutor> searchTutorType(int n,int pageSize,String keyword,String type){
        return  tutorRepository.findAll(new Specification<Tutor>() {
            @Override
            public Predicate toPredicate(Root<Tutor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword==null)
                    return cb.isTrue(root.get("enabled").as(Boolean.class));
                return  cb.and(cb.like(root.get(type).as(String.class),"%"+keyword+"%"),cb.isTrue(root.get("enabled").as(Boolean.class)));
            }
        },new PageRequest(n, pageSize));

    }


    /**
     * 根据导师姓名，导师职称，导师区域进行模糊查询，然后返回导师信息
     * @param n        显示的页数
     * @param pagesize 每页显示记录的条数
     * @param keyword  检索的关键字
     * @return         导师集合
     */
    public Page<Tutor> searchTutorAll(int n,int pagesize,String keyword){
        return  tutorRepository.findAll(new Specification<Tutor>() {
            @Override
            public Predicate toPredicate(Root<Tutor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword==null)
                    return null;
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)),cb.or(
                        cb.like(root.get("name").as(String.class), "%" + keyword + "%"),
                        cb.like(root.get("qualification").as(String.class),"%"+keyword+"%"),
                        cb.like(root.get("area").as(String.class),"%"+keyword+"%")
                ));
            }
        },new PageRequest(n, pagesize));

    }

    /**
     * 根据导师任职时间查询，然后返回导师信息
     * @param n        显示的页数
     * @param pagesize 每页显示记录的条数
     * @param start    起始时间
     * @param end      结束时间
     * @return         导师集合
     */
    public Page<Tutor> searchTutorDate(int n,int pagesize,Date start,Date end){
        return  tutorRepository.findAll(new Specification<Tutor>() {
            @Override
            public Predicate toPredicate(Root<Tutor> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (start==null&&end==null)
                    return null;
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)),cb.between(root.get("lastUploadDate").as(Date.class),start,end));
            }
        },new PageRequest(n, pagesize));

    }


    /**
     * 禁用一个导师
     * @param id
     */
    public void delTutor(Long id){
        Tutor tutor=findOneById(id);
        tutor.setEnabled(false);
        modify(tutor);
    }

    //增加一位导师
    public void addTutor(Tutor tutor){
        tutorRepository.save(tutor);
    }
    //修改一位导师信息
    public void modify(Tutor tutor){
        tutorRepository.save(tutor);

    }
    //查找一条考试消息
    public Tutor findOneById(Long id){
        return tutorRepository.findOne(id);
    }
}
