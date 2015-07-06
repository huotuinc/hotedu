package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Qa;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.management.loading.ClassLoaderRepository;

/**
 * Created by shiliting on 2015/6/25.
 *
 * @author shiliting
 */
@Repository
public interface QaRepository extends JpaRepository<Qa,Long>,ClassicsRepository<Qa>,JpaSpecificationExecutor<Qa> {

}
