package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Exam;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.AgentRepository;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.MemberService;
import org.junit.Ignore;
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
import java.util.Date;
import java.util.List;

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
    MemberService memberService;
    @Autowired
    AgentService agentService;

    public static final int PAGE_SIZE=10;   //分页，每页假设10条数据

    /**
     * 测试
     */
    @Test
    @Rollback
    @Ignore
    public void testAgent(){
        Agent agen = new Agent();
        agen.setEnabled(true);
        agen.setName("测试hahahahaha");
        Agent agent = agentService.addAgent(agen);

        ClassTeam classTea = new ClassTeam();
        classTea.setClassName("测试001");
        ClassTeam classTeam = agentService.agentAddClassTeam(agent, classTea);

        ClassTeam classTea2 = new ClassTeam();
        classTea.setClassName("测试002");
        ClassTeam classTeam2 = agentService.agentAddClassTeam(agent,classTea2);

        List<ClassTeam> classTeamsList = agentService.findAvailableClassTeams(agent);
        assertEquals("该代理商可用于分班的班级数量为2", 2, classTeamsList.size());
        Page<ClassTeam> classTeamPage = agentService.findClassArrangeExam(agent, 0, PAGE_SIZE, null, "all");
        assertEquals("该代理商的所有班级分页显示为2条", 2, classTeamPage.getTotalElements());

        Member _mb = new Member();
        _mb.setLoginName("测试001");
        _mb.setEnabled(true);
        _mb.setPayed(true);
        Member mb = memberService.addMember(agent,_mb);

        Member _mb2 = new Member();
        _mb2.setLoginName("测试001");
        _mb2.setEnabled(true);
        _mb2.setPayed(true);
        Member mb2 = memberService.addMember(agent,_mb2);

        Member _mb3 = new Member();
        _mb3.setLoginName("测试003");
        _mb3.setEnabled(true);
        _mb3.setPayed(true);
        Member mb3 =memberService.addMember(agent, _mb3);

        Page<Member> pages = agentService.findNoClassMembers(agent,0,PAGE_SIZE,null,"all");
        assertEquals("该代理商未分班的学员为3", 3, pages.getTotalElements());

        ArrayList<Long> arrayList= new ArrayList<Long>();
        arrayList.add(mb.getId());
        arrayList.add(mb2.getId());
        ArrayList<Long> arrayList2= new ArrayList<Long>();
        arrayList2.add(mb3.getId());

        agentService.arrangeClass(arrayList, classTeam);
        agentService.arrangeClass(arrayList2, classTeam2);
        Page<Member> pages2 = agentService.findNoClassMembers(agent,0,PAGE_SIZE,null,"all");
        assertEquals("该代理商未分班的学员为0", 0, pages2.getTotalElements());

        Exam exa = new Exam();
        exa.setExamName("测试001");
        exa.setExamAddress("测试");
        exa.setExamDate(new Date());
        Exam exam = agentService.addExam(agent,exa);
        List<Exam> examList = agentService.findAvailableExamTeams(agent);
        assertEquals("该代理商可以用于安排的考场数为1", 1, examList.size());

        ArrayList<Long> arrayList3= new ArrayList<Long>();
        arrayList3.add(classTeam.getId());
        arrayList3.add(classTeam2.getId());
        agentService.arrangeExam(arrayList3, exam);

        Page<Member> pages3 = agentService.findExamedMembers(agent,0,PAGE_SIZE,null,4,"passed");
        assertEquals("该代理商所有已经考试了的学员数量为3", 3, pages3.getTotalElements());

        agentService.setExamPassById(mb.getId(), 1);
        agentService.setExamPassById(mb2.getId(), 2);
        agentService.setExamPassById(mb3.getId(),1);

        pages3 = agentService.findExamedMembers(agent,0,PAGE_SIZE,null,1,"passed");
        assertEquals("该代理商所有通过考试了的学员数量为2", 2, pages3.getTotalElements());

        pages3 = agentService.findExamedMembers(agent,0,PAGE_SIZE,null,2,"passed");
        assertEquals("该代理商所有未通过考试了的学员数量为1",1,pages3.getTotalElements());

    }

}
