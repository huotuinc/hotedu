package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Video;
import com.huotu.hotedu.repository.VideoRepository;
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
 * Created by cwb on 2015/7/10.
 */
@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    //分页
    public Page<Video> searchVideo(int n, int pagesize, String keyword) {
        return videoRepository.findAll(new Specification<Video>() {
            @Override
            public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(keyword) || keyword == null) {
                    return null;
                }
                return cb.or(cb.like(root.get("videoName").as(String.class), "%" + keyword + "%"));
            }
        }, new PageRequest(n, pagesize));
    }

    public void delVideo(Long id) {
        videoRepository.delete(id);
    }

    public void addVideo(Video video) {
        videoRepository.save(video);
    }

    public void modifyVideo(Video video) {
        videoRepository.save(video);
    }

    public Video findOneById(Long id) {
        return videoRepository.findOne(id);
    }

    public List<Video> findByComplete(boolean complete) {
        return videoRepository.findByComplete(complete);
    }

    /**
     * 前台加载video页面
     */
    public Page<Video> loadPcSmallVideo(int n, int pageSize) {
        return videoRepository.findAll(new Specification<Video>() {
            @Override
            public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.isFalse(root.get("complete").as(Boolean.class));
            }
        }, new PageRequest(n, pageSize));
    }

    public Video findByVideoNo(int videoNo) {
        return videoRepository.findByVideoNo(videoNo);
    }
}
