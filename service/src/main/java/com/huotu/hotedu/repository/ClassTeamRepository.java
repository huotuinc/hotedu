package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.ClassTeam;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by jiashubing on 2015/7/21.
 *
 * @author jiashubing
 */
@Repository
public interface ClassTeamRepository extends JpaRepository<ClassTeam,Long>,ClassicsRepository<ClassTeam>,JpaSpecificationExecutor<ClassTeam>{


}
