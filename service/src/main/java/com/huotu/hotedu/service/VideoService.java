package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.entity.Video;
import com.huotu.hotedu.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by cwb on 2015/7/10.
 */
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    //加载视频列表
    public Page<Video> loadVideo(int n,int pagesize){
        return videoRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页
    public Page<Video> searchVideo(int n,int pagesize,String keyword) {
        return videoRepository.findAll(new Specification<Video>() {
            @Override
            public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.like(root.get("videoName").as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));
    }

    public void delVideo(Long id){
        videoRepository.delete(id);
    }

    public void addVideo(Video video){
        videoRepository.save(video);
    }

    public void modifyVideo(Video video){
        videoRepository.save(video);
    }

    public Video findOneById(Long id) {
        return videoRepository.findOne(id);
    }
}
