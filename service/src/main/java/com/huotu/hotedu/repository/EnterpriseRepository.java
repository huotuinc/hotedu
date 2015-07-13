package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Enterprise;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by shiliting on 2015/7/13.
 *
 * @author shiliting shiliting741@163.com
 */
@Repository
public interface EnterpriseRepository  extends JpaRepository<Enterprise,Long>,ClassicsRepository<Enterprise>,JpaSpecificationExecutor<Enterprise> {

}
