package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.AgentRepository;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.Assert.assertEquals;

/**
 * Created by jiashubing on 2015/7/21.
 *
 * @author jiashubing
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AgentServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    AgentService agentService;

    public static final int PAGE_SIZE=10;   //分页，每页假设10条数据

    /**
     * 测试 显示全部未分班和已经分班的学员
     */
    @Test
    @Rollback
    public void checkNoClassMember(){
        Member mb = new Member();
        mb.setLoginName("12345");
        mb.setEnabled(true);
        mb.setId((long) 100);
        memberService.addMember(mb);

//        ClassTeam classTeam = new ClassTeam();
//        classTeam.setId((long)20);
//        mb.setLoginName("12348");
//        mb.setEnabled(true);
//        mb.setId((long) 101);
//        mb.setTheClass(classTeam);
//        memberService.addMember(mb);

        Page<Member> pages = agentService.findNoClassMembers(0,PAGE_SIZE);
        assertEquals(1, pages.getTotalElements());

        pages = agentService.findHaveClassMembers(0,PAGE_SIZE);
        assertEquals(0, pages.getTotalElements());
    }

}
