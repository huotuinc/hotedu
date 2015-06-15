package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Repository
public interface LoginRepository extends JpaRepository<Login,Long>{

    Login findByLoginName(String loginName);


}
