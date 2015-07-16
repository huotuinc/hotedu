package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jiashubing on 2015/7/16.
 *
 * @author jiashubing luffy.ja at gmail.com
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


}
