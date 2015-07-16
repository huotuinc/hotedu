package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Agent;
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

import java.util.Date;
import java.util.Random;
import java.util.UUID;

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
public class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    MemberService memberService;
    public static final int PAGE_SIZE=10;   //分页，每页假设10条数据


    @Test
    @Rollback
    public void addMember() {
        Member mb = new Member();
        mb.setRealName(UUID.randomUUID().toString());
        String phoneNo = generateInexistentMobile(memberRepository);
        mb.setPhoneNo(phoneNo);
        mb.setSex(new Random().nextInt(1));
        mb.setArea("杭州");
        memberService.addMember(mb);
        assertEquals(mb.getLoginName(), memberRepository.findByPhoneNo(phoneNo).getLoginName());
    }

    /**
     * 测试  显示某个代理商名下的所有学员
     */
    @Test
    @Rollback
    public void loadMembersByAgent(){
        Member member=new Member();
        member.setLoginName("kkk");
        Agent agent=agentRepository.getOne((long)3);
        Page<Member> pages= memberService.loadMembersByAgent(agent, 0, PAGE_SIZE);
        assertEquals(1,pages.getTotalElements());

    }


    /**
     * 返回一个不存在的手机号码
     *
     * @param memberRepository
     * @return 手机号码
     */
    public static String generateInexistentMobile(MemberRepository memberRepository) {
        while (true) {
            String number = "" + System.currentTimeMillis();
            number = number.substring(number.length() - 11);
            char[] data = number.toCharArray();
            data[0] = '1';
            number = new String(data);
            if (memberRepository.count()==0)
                return number;
            if (memberRepository.countByPhoneNo(number) == 0) {
                return number;
            }
        }
    }




    @Test
    @Rollback
    public void addOneMember(){

        Member member=new Member();
        member.setPayDate(new Date());
        member.setTheClass(new ClassTeam());
        member.setEnabled(true);
        member.setPhoneNo("18069875423");
        member.setApplyDate(new Date(System.currentTimeMillis()));
        member.setConfirmPerson("slt");
        member.setIsPassed(false);
        member.setAgent(new Agent());
        member.setPictureUri("/images/");
        member.setIsPayed(true);
        member.setSex(1);
        member.setPassword("4a5sd4faw5ea3s2d");
        member.setLastLoginDate(new Date());
        member.setLastLoginIP("120.125.30.5");
        member.setLoginName("asdfedsa");
        member.setRealName("test1");
        member.setLoginName("slttt");
        member.setArea("杭州");
        memberService.addMember(member);


        }




}
