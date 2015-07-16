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

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by jiashubing on 2015/7/16.
 *
 * @author jiashubing at gmail.com
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
    public void addMember() {
        Member mb = new Member();
        mb.setRealName(UUID.randomUUID().toString());
        String phoneNo = generateInexistentMobile(memberRepository);
        mb.setPhoneNo(phoneNo);
        mb.setSex(new Random().nextInt(1));
        mb.setArea("杭州");
        memberService.addMember(mb);
        assertEquals(mb.getLoginName(),memberRepository.findByPhoneNo(phoneNo).getLoginName());
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

}
