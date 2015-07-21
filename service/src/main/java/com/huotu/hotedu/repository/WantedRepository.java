package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Wanted;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by cwb on 2015/7/10.
 */
public interface WantedRepository extends JpaRepository<Wanted,Long>,ClassicsRepository<Wanted>,JpaSpecificationExecutor<Wanted> {
}
