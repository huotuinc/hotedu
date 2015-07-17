package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by cwb on 2015/7/15.
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;


    /**
     * 增加学员
     * @param mb 学员对象
     */
    public void addMember(Member mb) {
        memberRepository.save(mb);
    }

    /**
     * 检测手机号是否被注册
     */
    public boolean isPhoneNoExist(String phoneNo) {
        boolean exist = false;
        Member mb = memberRepository.findByPhoneNo(phoneNo);
        if(mb!=null) {
            exist = true;
        }
        return exist;
    }

    /**
     * 该方法用来返回某个代理商名下的所有学员信息，并且以分页的形式返回
     * @param n               第几页
     * @param pagesize        每页记录条数
     * @param agent           代理商帐户名
     * @return                学员集合
     */
    public Page<Member>  loadMembersByAgent(Agent agent,Integer n,Integer pagesize){

        return  memberRepository.findAll(new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (agent==null||n==null||pagesize==null) {
                    return null;
                }
                return cb.and(cb.equal(root.get("agent").as(Agent.class), agent), cb.isTrue(root.get("enabled").as(boolean.class)));
            }
        },new PageRequest(n, pagesize));
    }

    /**
     * 显示所有学员 每页10条
     * @param n         第几页
     * @param pagesize  每页几条
     * @return          学员集合
     */
    public Page<Member> loadMembers(Integer n,Integer pagesize){
        return memberRepository.findAll(new PageRequest(n,pagesize));
    }

    public Member findOneById(Long id){
        return memberRepository.findOne(id);
    }

    /**
     * 删除学员，设为不可用
     * @param id 学员id
     */
    public void delMember(Long id){
            Member mb= findOneById(id);
            mb.setEnabled(false);
            memberRepository.save(mb);
    }

    /**
     * 确认交费
     * @param id 学员id
     */
    public void checkPay(Long id){
        Member mb= findOneById(id);
        mb.setIsPayed(true);
        memberRepository.save(mb);
    }

    public Member findOneByLoginName(String loginName) {
        Member mb = memberRepository.findByLoginName(loginName);
        return mb;
    }
}
