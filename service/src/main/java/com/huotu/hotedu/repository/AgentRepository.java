package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Agent;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by shiliting on 2015/7/16.
 * @author shiliting shiliting741@163.com
 */
@Repository
public interface AgentRepository extends JpaRepository<Agent,Long>,ClassicsRepository<Agent>,JpaSpecificationExecutor<Agent> {

}
