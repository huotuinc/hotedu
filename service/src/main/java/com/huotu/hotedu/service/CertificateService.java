package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Certificate;
import com.huotu.hotedu.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2015/8/6.
 */
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;


    public Certificate addCertificate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

}
