package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Certificate;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cwb on 2015/8/6.
 */
@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;


    public Certificate addCertificate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    public Certificate findOneByMember(Member member){
        return certificateRepository.findBymember(member);
    }

    public Certificate findOneBycertificateNo(String no){
        return certificateRepository.findBycertificateNo(no);
    }
    public Certificate findOneById(Long id){
        return certificateRepository.findOne(id);
    }
    public void modifyCertificate(Certificate certificate){
        certificateRepository.save(certificate);
    }

}
