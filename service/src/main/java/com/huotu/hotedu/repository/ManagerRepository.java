package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Manager;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long>,JpaSpecificationExecutor<Manager>,ClassicsRepository<Manager> {

    @Modifying
    @Query("update Manager u set u.managerField=?1")
    int updateManagerField(String managerField);

}
