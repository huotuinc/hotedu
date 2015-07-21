package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Exam;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.AgentRepository;
import com.huotu.hotedu.repository.MemberRepository;
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


import java.util.ArrayList;

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

        ClassTeam classTeam = new ClassTeam();
        classTeam.setId((long) 20);
        agentService.addClassTeam(classTeam);

        Member mb2 = new Member();
        mb2.setLoginName("12348");
        mb2.setEnabled(true);
        mb2.setId((long) 101);
        mb2.setTheClass(classTeam);
        memberService.addMember(mb2);

        Page<Member> pages = agentService.findNoClassMembers(0,PAGE_SIZE);
        assertEquals("没有班级的学员为1",1, pages.getTotalElements());

        Page<Member> pages2 = agentService.findHaveClassMembers(0,PAGE_SIZE);
        assertEquals("有班级的学员数量为1",1, pages2.getTotalElements());
    }

    /**
     * 测试分班
     */
    @Test
    @Rollback
    public void checkArrangeClass(){
        Member mb = new Member();
        mb.setLoginName("1111");
        mb.setEnabled(true);
        mb.setId((long) 100);
        memberService.addMember(mb);
        mb.setLoginName("2222");
        mb.setEnabled(true);
        mb.setId((long) 101);
        memberService.addMember(mb);
        ClassTeam classTeam = new ClassTeam();
        classTeam.setId((long) 111);
        classTeam.setClassName("nihao");
        ArrayList<Integer>list = new ArrayList<Integer>(13);
        agentService.arrangeClass(list, classTeam);
//        assertEquals("分班后第2个成员的班级","nihao",mb.getTheClass().getClassName());
    }

    /**
     * 测试 注册班级
     * 代理商添加班级
     * 查找代理商所拥有的班级
     */
    @Test
    @Rollback
    public void checkFindExistClassAll(){
        Agent agent = new Agent();
        agent.setId((long) 50);
        agent.setEnabled(true);
        agentService.addAgent(agent);

        Exam exam = new Exam();

        ClassTeam classTeam = new ClassTeam();
        classTeam.setId((long) 12);
        classTeam.setAgent(agent);
        classTeam.setExam(exam);
        agentService.agentAddClassTeam(agent, classTeam);

        ClassTeam classTeam2 = new ClassTeam();
        classTeam2.setId((long) 13);
        classTeam2.setAgent(agent);
        classTeam2.setExam(exam);
        agentService.agentAddClassTeam(agent,classTeam2);

        Page<ClassTeam> pages = agentService.findExistClassAll(0,10,agent);
        assertEquals("该代理商所拥有的班级数目为2",2, pages.getTotalElements());

    }

}
