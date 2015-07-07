package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Huotu;
import com.huotu.hotedu.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shiliting 2015/6/25.
 *
 * @author shiliting
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    //返回我们的公司
    public Huotu loadCompany(Long id){
        return companyRepository.findOne(id);
    }

    //修改我们的公司
}
