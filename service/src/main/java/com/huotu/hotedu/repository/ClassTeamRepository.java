package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jiashubing on 2015/7/21.
 *
 * @author jiashubing
 */
@Repository
public interface ClassTeamRepository extends JpaRepository<ClassTeam,Long>,ClassicsRepository<ClassTeam>,JpaSpecificationExecutor<ClassTeam>{

//ClassTeam findByClassName(String className);

    ClassTeam findOneById(long id);

    ClassTeam findByClassName(String className);
    List<ClassTeam> findByAgent(Agent agent);
}
