package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.entity.Tutor;
import com.huotu.hotedu.repository.AgentRepository;
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
 * Created by shiliting 2015/7/20.
 * 代理商相关的Service
 * @author shiliting
 */
@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 返回所有代理商
     * @param n         第几页
     * @param pageSize  每页几条记录
     * @return          代理商集合
     */
    public Page<Agent> loadagents(int n,int pageSize){

        return agentRepository.findAll(new Specification<Agent>() {
            @Override
            public Predicate toPredicate(Root<Agent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.isTrue(root.get("enabled").as(Boolean.class));
            }
        },
        new PageRequest(n,pageSize));
    }



    /**
     * 返回按照类型和关键字搜索过之后的代理商
     * @param n         第几页
     * @param pageSize  每页几条记录
     * @param keyword   搜索关键字
     * @param type      搜索类型
     * @return          代理商集合
     */
    public Page<Agent> searchAgentType(int n,int pageSize,String keyword,String type){
       return  agentRepository.findAll(new Specification<Agent>() {
            @Override
            public Predicate toPredicate(Root<Agent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)),cb.like(root.get(type).as(String.class), "%" + keyword + "%"));
            }
        },new PageRequest(n, pageSize));
    }

    /**
     * 分页依据全部搜索 搜索的代理商
     * @param n             第几页
     * @param pagesize      每页显示几条
     * @param keyword       关键词
     * @return              代理商集合
     */
    public Page<Agent> searchAgentAll(int n,int pagesize,String keyword){
        return  agentRepository.findAll(new Specification<Agent>() {
            @Override
            public Predicate toPredicate(Root<Agent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword==null)
                    return null;
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)), cb.or(
                        cb.like(root.get("name").as(String.class), "%" + keyword + "%"),
                        cb.like(root.get("area").as(String.class), "%" + keyword + "%"),
                        cb.like(root.get("LoginName").as(String.class), "%" + keyword + "%")
                ));
            }
        },new PageRequest(n, pageSize));

    }

    /**
     * 禁用代理商
     * @param id 代理商id
     */
    public void delAgent(Long id){
        Agent agent=findOneById(id);
        agent.setEnabled(false);
        modify(agent);
    }

    /**
     * 增加一个代理商
     * @param agent 代理商对象
     */
    public void addAgent(Agent agent){agentRepository.save(agent);}

    /**
     * 修改一位代理商
     * @param agent 代理商对象
     */
    public void modify(Agent agent){agentRepository.save(agent);}


    /**
     * 查找一位代理商
     * @param id 代理商id
     * @return 代理商对象
     */
    public Agent findOneById(Long id){return agentRepository.findOne(id);}

    /**
     * 显示所有未分班学员 每页10条
     * @param n         第几页
     * @param pagesize  每页几条
     * @return          学员集合
     */
    public Page<Member> findNoClassMembers(Integer n,Integer pagesize){
        return  memberRepository.findAll(new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)), cb.isNull(root.get("theClass").as(ClassTeam.class)));
            }
        }, new PageRequest(n, pagesize));
    }

    /**
     * 显示所有已分班学员 每页10条
     * @param n         第几页
     * @param pagesize  每页几条
     * @return          学员集合
     */
    public Page<Member> findHaveClassMembers(Integer n,Integer pagesize){
        return  memberRepository.findAll(new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)),cb.isNotNull(root.get("theClass").as(ClassTeam.class)));
            }
        },new PageRequest(n, pagesize));

    }


}
