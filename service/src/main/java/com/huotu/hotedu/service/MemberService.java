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
}
