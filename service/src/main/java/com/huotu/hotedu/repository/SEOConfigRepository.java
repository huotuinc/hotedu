package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.SEOConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2015/11/12.
 */
@Repository
public interface SEOConfigRepository extends JpaRepository<SEOConfig,Long> {
}
