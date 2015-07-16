package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Member;
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
    MemberService memberService;
    public static final int PAGE_SIZE=10;   //分页，每页假设10条数据

    @Test
    @Rollback
    public void member(){
        Long id = (long)50;
        Member member = new Member();
        member.setEnabled(true);
        member.setId(id);
        member.setIsPayed(false);
        memberService.addMember(member);
        System.out.println("添加学员");

        member= memberService.findOneById(id);
        System.out.println("学员姓名：" + member.getLoginName() + "  学员是否可用: " + member.isEnabled());

        memberService.delMember(id);
        System.out.println("删除后学员是否可用: " + member.isEnabled());

        memberService.checkPay(id);
        System.out.println("确认缴费: " + member.isPayed());

    }

    @Test
    public void loadMembers(){
        Page<Member> pages= memberService.loadMembers("agent", 0, PAGE_SIZE);
        System.out.println("pages:"+pages.toString());
        System.out.println("分页后学员总数："+pages.getSize());
        System.out.println("总记录数："+pages.getTotalElements());
        for(Member m:pages){
            System.out.println("学员信息："+m.toString());
        }

    }


}
