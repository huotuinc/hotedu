package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Exam;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiashubing on 2015/7/30.
 * @author jiashubing
 */
@Repository
public interface ExamRepository extends JpaRepository<Exam,Long>,ClassicsRepository<Exam>,JpaSpecificationExecutor<Exam> {

    Exam findByExamName(String examName);
    List<Exam> findByAgent(Agent agent);

}
