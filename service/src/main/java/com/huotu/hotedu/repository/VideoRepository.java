package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.entity.Video;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by cwb on 2015/7/10.
 */
public interface VideoRepository extends JpaRepository<Video,Long>,ClassicsRepository<Video>,JpaSpecificationExecutor<Video> {
}