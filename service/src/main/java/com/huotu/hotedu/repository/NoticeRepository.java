package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by WenbinChen on 2015/10/28 11:37.
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long>,JpaSpecificationExecutor<Notice> {
    List<Notice> findByEnabled(boolean enabled);
}
