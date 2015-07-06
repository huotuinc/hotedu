package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Link;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jiashubing
 */
@Repository
public interface LinkRepository extends JpaRepository<Link,Long>,ClassicsRepository<Link>,JpaSpecificationExecutor<Link>{

}
