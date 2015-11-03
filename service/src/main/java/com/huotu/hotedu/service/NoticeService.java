package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Notice;
import com.huotu.hotedu.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3.
 */
@Service
public class NoticeService {

    @Autowired
    NoticeRepository noticeRepository;

    public Page<Notice> getPage(int n, int pageSize, Boolean b) {
        return noticeRepository.findAll(new Specification<Notice>() {
            @Override
            public Predicate toPredicate(Root<Notice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if(b==null) {
                    return null;
                }
                return cb.equal(root.get("enabled").as(Boolean.class), b);
            }
        },new PageRequest(n, pageSize,new Sort(Sort.Direction.DESC,"lastUpdateTime")));
    }
}
