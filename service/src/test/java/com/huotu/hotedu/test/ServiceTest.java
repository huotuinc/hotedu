package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Login;
import com.huotu.hotedu.entity.Manager;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.LoginRepository;
import com.huotu.hotedu.repository.ManagerRepository;
import com.huotu.hotedu.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ServiceTest {

    @Autowired
    LoginRepository loginRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ManagerRepository managerRepository;

    @Test
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
    }
}
