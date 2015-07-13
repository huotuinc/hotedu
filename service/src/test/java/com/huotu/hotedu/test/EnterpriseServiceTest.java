package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Enterprise;
import com.huotu.hotedu.repository.EnterpriseRepository;
import com.huotu.hotedu.service.EnterpriseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class EnterpriseServiceTest {


    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    EnterpriseRepository enterpriseRepository;


    @Test
    public void loadEnterprise(){
        Enterprise enterprise=new Enterprise();
        enterprise.setLastUploadDate(new Date(System.currentTimeMillis()-1024*1024*3));
        enterprise.setName("enterprisetest");
        enterprise.setInformation("这是一个神奇的企业");
        enterprise.setIsPutaway(false);
        enterprise.setTel("18065479853");
        enterprise.setLogoUri("/images/company/");
        enterpriseService.addEnterprise(enterprise);

        enterprise=new Enterprise();
        enterprise.setLastUploadDate(new Date(System.currentTimeMillis()-1024*1024*2));
        enterprise.setName("enterprisetest2");
        enterprise.setInformation("这是一个神奇的企业2");
        enterprise.setIsPutaway(false);
        enterprise.setTel("18065479855");
        enterprise.setLogoUri("/images/company/");
        enterpriseService.addEnterprise(enterprise);


    }

    @Test
    public void searchEnterprise(){
        Page<Enterprise> pages= enterpriseService.loadEnterprise(0,10);
        for(Enterprise e :pages){
            System.out.println(e);
        }
    }

//    @Test
//    public void modifyEnterprise(){
//        Enterprise enterprise=new Enterprise();
//        enterprise=new Enterprise();
//        enterprise.setLastUploadDate(new Date(System.currentTimeMillis()-1024*1024*1));
//        enterprise.setName("enterprisetest3");
//        enterprise.setInformation("这是一个神奇的企业3");
//        enterprise.setIsPutaway(true);
//        enterprise.setTel("18065479853");
//        enterprise.setLogoUri("/images/company/");
//        enterpriseService.addEnterprise(enterprise);
//
//    }
//
//    @Test
//    public void delEnterprise(){
//        enterpriseService.delEnterprise((long) 9);
//    }




}
