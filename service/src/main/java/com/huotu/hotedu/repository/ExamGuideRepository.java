package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.ExamGuide;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by shiliting on 2015/6/25.
 *
 * @author shiliting
 */
@Repository
public interface ExamGuideRepository extends JpaRepository<ExamGuide,Long>,ClassicsRepository<ExamGuide>,JpaSpecificationExecutor<ExamGuide>{


}
