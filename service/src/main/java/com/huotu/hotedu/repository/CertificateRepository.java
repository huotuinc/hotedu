package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.Certificate;
import com.huotu.hotedu.entity.Member;
import org.luffy.lib.libspring.data.ClassicsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by jiashubing on 2015/8/6.
 *
 * @author jiashubing
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate,Long>,ClassicsRepository<Certificate>,JpaSpecificationExecutor<Certificate>{
    Certificate findByMember(Member member);
    Certificate findByCertificateNo(String no);
}
