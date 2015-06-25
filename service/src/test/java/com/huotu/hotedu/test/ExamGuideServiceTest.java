package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Login;
import com.huotu.hotedu.entity.Manager;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.repository.LoginRepository;
import com.huotu.hotedu.repository.ManagerRepository;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.service.ExamGuideService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ExamGuideServiceTest {

    @Autowired
    LoginRepository loginRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ManagerRepository managerRepository;
    @Autowired
    ExamGuideRepository examGuideRepository;
    @Autowired
    private ExamGuideService guideService;


    @Test
    public void loadExamGuide(){
        ExamGuide examGuide=new ExamGuide();
        examGuide.setContent("123456");
        examGuide.setTitle("我的第一次测试");
        examGuide.setTop(false);
        examGuide.setLastUploadDate(new Date());
        examGuideRepository.save(examGuide);
        List<ExamGuide> list=guideService.loadExamGuide();
        System.out.println(list);
    }


    @Test
//    @Rollback
    public void justgo(){
        System.out.println("ServiceTest.justgo");
        Member member = new Member();
        member.setLoginName("iammember");
        member.setMemeberField("yoha");

        member = memberRepository.save(member);

        Manager manager = new Manager();
        manager.setLoginName("iammanager");
        manager.setManagerField("oha");
        manager = managerRepository.save(manager);

        Login otherMember = loginRepository.findByLoginName(member.getLoginName());
        Login otherManager = loginRepository.findByLoginName(manager.getLoginName());

        Login thisone = loginRepository.findOne(manager.getId());
        System.out.println("ServiceTest.justgo");

        //PO JO test
        manager.setManagerField("foo");
        managerRepository.save(manager);

        assertEquals("foo", managerRepository.getOne(manager.getId()).getManagerField());

        managerRepository.updateManagerField("bar");
        System.out.println(manager.getManagerField());
        managerRepository.refresh(manager);
//        Manager newManager = entityManagerFactory.createEntityManager().merge(manager);
        assertEquals("bar", managerRepository.getOne(manager.getId()).getManagerField());
    }
}
