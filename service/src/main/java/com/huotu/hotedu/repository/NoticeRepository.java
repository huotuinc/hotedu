package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by WenbinChen on 2015/10/28 11:37.
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Notice findByEnabled(boolean enabled);
}
