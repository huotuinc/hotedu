package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Member;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by luffy on 2015/6/10.
 * Modify by shiliting on 2015/7/15  shiliting741@163.com
 * @author luffy luffy.ja at gmail.com
 */
@Repository
public interface MemberRepository  extends JpaRepository<Member,Long>,ClassicsRepository<Member>,JpaSpecificationExecutor<Member> {

   int countByPhoneNo(String phoneNo);

    Member findByPhoneNo(String phoneNo);

    Member findByLoginName(String loginName);

}
