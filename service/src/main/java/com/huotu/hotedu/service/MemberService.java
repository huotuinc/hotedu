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
 * Modify by shiliting on 2015/7/24
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;


    /**
     * 增加学员
     * @param mb 学员对象
     * @return 持久化学员对象
     */
    public Member addMember(Member mb) {
        return memberRepository.save(mb);
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
     * 跳转到学院搜索的重载方法主要用于查询非搜索的会员
     * @param agent      会员属于的代理商
     * @param pageNo     显示第几页(0是第一页)
     * @param pageSize   每页显示的记录条数(默认10条)
     * @return           会员集合
     */
    public Page<Member> searchMembers(Agent agent,Integer pageNo,Integer pageSize){
        return searchMembers(agent,pageNo,pageSize,null,null);
    }

    /**
     * 搜索会员
     * @param agent      会员属于的代理商
     * @param pageNo     显示第几页(0是第一页)
     * @param pageSize   每页显示的记录条数(默认10条)
     * @param keyword    搜索会员的关键字
     * @param type       搜索会员的类型
     * @return           会员集合
     */
    public Page<Member> searchMembers(Agent agent,Integer pageNo,Integer pageSize,String keyword,String type){
        return memberRepository.findAll(new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(keyword)||keyword==null) {
                    return cb.and(cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.isTrue(root.get("enabled").as(boolean.class))
                    );
                }else if("all".equals(type)){
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.isTrue(root.get("enabled").as(boolean.class)),
                            cb.or(
                                    cb.like(root.get("realName").as(String.class),"%"+keyword+"%"),
                                    cb.like(root.get("phoneNo").as(String.class),"%"+keyword+"%")
                                    //cb.like(root.get("theClass.").as(String.class),"%"+keyword+"%")
                            )
                    );
                }else{
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.isTrue(root.get("enabled").as(boolean.class)),
                            cb.equal(root.get(type).as(boolean.class),"是".equals(keyword)? true:false)
                    );

                }
            }
        },new PageRequest(pageNo,pageSize));
    }


    /**
     * 查找学员
     * @param id 学员id
     * @return  member.html
     */
    public Member findOneById(Long id){
        return memberRepository.findOne(id);
    }

    /**
     * 禁用学员
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
        mb.setPayed(true);
        memberRepository.save(mb);
    }

    /**
     * 通过用户名查找学员
     * @param loginName 用户名
     * @return          学员
     */
    public Member findOneByLoginName(String loginName) {
        Member mb = memberRepository.findByLoginName(loginName);
        return mb;
    }


}
