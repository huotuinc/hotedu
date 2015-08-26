package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Certificate;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cwb on 2015/8/6.
 */
@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Transactional
    public Certificate addCertificate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Transactional
    public Certificate deletePic(Certificate certificate) {
        certificate.setPictureUri("");
        return certificateRepository.save(certificate);
    }

    public Certificate findOneByMember(Member member){
        return certificateRepository.findByMember(member);
    }

    public Certificate findOneByCertificateNo(String no){
        return certificateRepository.findByCertificateNo(no);
    }
    public Certificate findOneById(Long id){
        return certificateRepository.findOne(id);
    }
    @Transactional
    public void modifyCertificate(Certificate certificate){
        certificateRepository.save(certificate);
    }

}
