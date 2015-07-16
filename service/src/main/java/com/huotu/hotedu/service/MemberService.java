package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by cwb on 2015/7/15.
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public void addMember(Member mb) {
        memberRepository.save(mb);
    }

    public Member findOneById(Long id){
        return memberRepository.findOne(id);
    }

    /**
     * 删除学员，设为不可用
     * @param id
     */
    public void delMember(Long id){
            Member mb= findOneById(id);
            mb.setEnabled(false);
    }

    /**
     * 确认交费
     * @param id
     */
    public void checkPay(Long id){
        Member mb= findOneById(id);
        mb.setIsPayed(true);
    }
}
