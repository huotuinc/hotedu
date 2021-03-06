package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private ClassTeamRepository classTeamRepository;

    @Autowired
    private ExamRepository examRepository;

    /**
     * 添加一位代理商
     * @param agent 代理商对象
     */
    @Transactional
    public Agent addAgent(Agent agent){
        return agentRepository.save(agent);
    }

    /**
     * 查找一位代理商所属区域
     * @param areaId 代理商id
     * @return 代理商对象
     */
    public Agent findByAreaId(String areaId){return agentRepository.findByAreaId(areaId);}


    /**
     * 查找一位代理商
     * @param id 代理商id
     * @return 代理商对象
     */
    public Agent findOneById(Long id){return agentRepository.findOne(id);}

    /**
     * Created by jiashubing on 2015/7/24.
     * 查找一个已有班级
     * @param id 班级id
     * @return 班级对象
     */
    public ClassTeam findClassTeamById(long id) {
        return classTeamRepository.findOne(id);
    }

    /**
     * Created by jiashubing on 2015/8/1.
     * 修改班级名称
     * @param id        班级id
     * @param className 修改名称
     * @return      班级对象
     */
    @Transactional
    public ClassTeam modifyClassTeamName(Long id, String className) {
        ClassTeam classTeam = classTeamRepository.findOne(id);
        classTeam.setClassName(className);
        return classTeamRepository.save(classTeam);
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 查找一个已有考场
     * @param id    考场id
     * @return      考场对象
     */
    public Exam findExamById(long id) {
        return examRepository.findOne(id);
    }

    /**
     * Created by cwb on 2015/7/31.
     * 检查该考场名称是否已经使用
     * @param examName     考场名称
     * @return             若没有使用，返回true；若使用过，则返回false
     */
    public boolean isExamNameAvailable(String examName) {
        Exam exam = examRepository.findByExamName(examName);
        if(exam==null) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * Created by jiashubing on 2015/7/31.
     * 检查该考场名称是否已经使用
     * @param className     班级名称
     * @return             若没有使用，返回true；若使用过，则返回false
     */
    public boolean isClassTeamNameAvailable(String className) {
        ClassTeam classTeam = classTeamRepository.findByClassName(className);
        if(classTeam==null) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 显示所有未分班学员 每页10条
     * 加载、搜索、上一页、下一页
     * @param agent         当前代理商
     * @param pageNo        第几页
     * @param pageSize      每页几条
     * @param keywords      关键词
     * @param searchSort    搜索类型
     * @return              学员集合
     */
    public Page<Member> findNoClassMembers(Agent agent,Integer pageNo,Integer pageSize,String keywords,String searchSort){
        return  memberRepository.findAll(new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(keywords) || keywords == null) {
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.isTrue(root.get("enabled").as(Boolean.class)),
                            cb.isNull(root.get("theClass").as(ClassTeam.class)),
                            cb.isTrue(root.get("payed").as(Boolean.class))
                    );
                } else if ("all".equals(searchSort)) {
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.isTrue(root.get("enabled").as(boolean.class)),
                            cb.isNull(root.get("theClass").as(ClassTeam.class)),
                            cb.isTrue(root.get("payed").as(Boolean.class)),
                            cb.or(
                                    cb.like(root.get("realName").as(String.class), "%" + keywords + "%"),
                                    cb.like(root.get("phoneNo").as(String.class), "%" + keywords + "%"),
                                    cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%")
                            )
                    );
                } else {
                    if ("area".equals(searchSort)) {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(boolean.class)),
                                cb.isNull(root.get("theClass").as(ClassTeam.class)),
                                cb.isTrue(root.get("payed").as(Boolean.class)),
                                cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%")
                        );
                    } else {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(boolean.class)),
                                cb.isNull(root.get("theClass").as(ClassTeam.class)),
                                cb.isTrue(root.get("payed").as(Boolean.class)),
                                cb.like(root.get(searchSort).as(String.class), "%" + keywords + "%")
                        );
                    }
                }
            }
        }, new PageRequest(pageNo, pageSize));
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 显示所有已考试学员 每页10条
     * 加载、搜索、上一页、下一页
     * @param agent         当前代理商
     * @param pageNo        第几页
     * @param pageSize      每页几条
     * @param keywords      关键词
     * @param searchSort    搜索类型
     * @return              学员集合
     */
    public Page<Member> findExamedMembers(Agent agent,Integer pageNo,Integer pageSize,String keywords,Integer passedSortText,String searchSort){
        return  memberRepository.findAll(new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if("passed".equals(searchSort)){
                    if(passedSortText==4){
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(Boolean.class)),
                                cb.isTrue(root.get("payed").as(Boolean.class)),
                                cb.isNotNull(root.get("theClass").as(ClassTeam.class)),
                                cb.greaterThan(cb.currentTimestamp(),root.get("theClass").get("exam").get("examDate")),
                                cb.notEqual(root.get("certificateStatus").as(Integer.class),1)
                        );
                    }
                    else{
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(boolean.class)),
                                cb.isTrue(root.get("payed").as(Boolean.class)),
                                cb.greaterThan(cb.currentTimestamp(),root.get("theClass").get("exam").get("examDate")),
                                cb.notEqual(root.get("certificateStatus").as(int.class), 1),
                                cb.equal(root.get("passed").as(Integer.class), passedSortText)
                        );
                    }
                }
                else {
                    if (("".equals(keywords) || keywords == null)) {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(Boolean.class)),
                                cb.isTrue(root.get("payed").as(Boolean.class)),
                                cb.greaterThan(cb.currentTimestamp(),root.get("theClass").get("exam").get("examDate")),
                                cb.notEqual(root.get("certificateStatus").as(int.class), 1)
                        );
                    } else if ("all".equals(searchSort)) {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(boolean.class)),
                                cb.isTrue(root.get("payed").as(Boolean.class)),
                                cb.greaterThan(cb.currentTimestamp(),root.get("theClass").get("exam").get("examDate")),
                                cb.notEqual(root.get("certificateStatus").as(int.class), 1),
                                cb.or(
                                        cb.like(root.get("realName").as(String.class), "%" + keywords + "%"),
                                        cb.like(root.get("phoneNo").as(String.class), "%" + keywords + "%"),
                                        cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%"),
                                        cb.like(root.get("theClass").get("className").as(String.class), "%" + keywords + "%")
                                )
                        );
                    } else {
                        if ("area".equals(searchSort)) {
                            return cb.and(
                                    cb.equal(root.get("agent").as(Agent.class), agent),
                                    cb.isTrue(root.get("enabled").as(boolean.class)),
                                    cb.isTrue(root.get("payed").as(Boolean.class)),
                                    cb.greaterThan(cb.currentTimestamp(),root.get("theClass").get("exam").get("examDate")),
                                    cb.notEqual(root.get("certificateStatus").as(int.class), 1),
                                    cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%")
                            );
                        } else if ("className".equals(searchSort)) {
                            return cb.and(
                                    cb.equal(root.get("agent").as(Agent.class), agent),
                                    cb.isTrue(root.get("enabled").as(boolean.class)),
                                    cb.isTrue(root.get("payed").as(Boolean.class)),
                                    cb.greaterThan(cb.currentTimestamp(),root.get("theClass").get("exam").get("examDate")),
                                    cb.notEqual(root.get("certificateStatus").as(int.class), 1),
                                    cb.like(root.get("theClass").get("className").as(String.class), "%" + keywords + "%")
                            );
                        } else {
                            return cb.and(
                                    cb.equal(root.get("agent").as(Agent.class), agent),
                                    cb.isTrue(root.get("enabled").as(boolean.class)),
                                    cb.isTrue(root.get("payed").as(Boolean.class)),
                                    cb.greaterThan(cb.currentTimestamp(),root.get("theClass").get("exam").get("examDate")),
                                    cb.notEqual(root.get("certificateStatus").as(int.class), 1),
                                    cb.like(root.get(searchSort).as(String.class), "%" + keywords + "%")
                            );
                        }
                    }
                }
            }
        }, new PageRequest(pageNo, pageSize));
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 安排分班
     * @param allNoClassMemberList  需要分班的成员
     * @param classTeam             分配的班级
     */
    @Transactional
    public void arrangeClass(ArrayList<Long> allNoClassMemberList,ClassTeam classTeam){
        Member mb = null;
        int memberNum = 0;
        for(int i=0; i<allNoClassMemberList.size();i++){
            mb = memberRepository.findOne(allNoClassMemberList.get(i));
            mb.setTheClass(classTeam);
            memberNum ++;
            memberRepository.save(mb);
        }
        classTeam.setMemberNum(classTeam.getMemberNum()+memberNum);
        classTeamRepository.save(classTeam);
    }

    /**
     * Created by jiashubing on 2015/7/30.
     * 安排分考场
     * @param classExamArrayLis  需要分考场的班级
     * @param exam             分配的考场
     */
    @Transactional
    public void arrangeExam(ArrayList<Long> classExamArrayLis,Exam exam){
        ClassTeam ct = null;
        for(int i=0; i<classExamArrayLis.size();i++){
            ct = classTeamRepository.findOne(classExamArrayLis.get(i));
            ct.setExam(exam);
            classTeamRepository.save(ct);
        }
    }

    /**
     * Modified by cwb on 2015/7/31
     * Created by jiashubing on 2015/7/24.
     * 增加班级
     * @param classTeam  班级名字
     */
    @Transactional
    public ClassTeam addClassTeam(ClassTeam classTeam){
        return classTeamRepository.save(classTeam);
    }

    /**
     * Created by jiashubing on 2015/7/31.
     * 增加考场
     * @param newExam  增加的考场
     */
    @Transactional
    public Exam addExam(Agent agent,Exam newExam){
        newExam.setAgent(agent);
        Exam exam = examRepository.save(newExam);
        return exam;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 代理商添加班级
     * @param agent         代理商
     * @param classTeam     添加的班级
     */
    @Transactional
    public ClassTeam agentAddClassTeam(Agent agent,ClassTeam classTeam){
        classTeam.setAgent(agent);
        return classTeamRepository.save(classTeam);
    }

    /**
     * Created by cwb on 2015/7/29
     * 根据代理商查找可用于分班的班级
     * @param agent 当前代理商
     * @return      班级集合
     */
    public List<ClassTeam> findAvailableClassTeams(Agent agent) {
        List<ClassTeam> classTeams = classTeamRepository.findByAgent(agent);
        return classTeams;
    }

    /**
     * Created by jiashubing on 2015/7/30
     * 根据代理商查找用于安排班级的考场
     * @param agent 当前代理商
     * @return      考场集合
     */
    public List<Exam> findAvailableExamTeams(Agent agent) {
        List<Exam> exam = examRepository.findByAgent(agent);
        return exam;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 显示代理商所有的班级 安排考场 分页显示
     * 加载、搜索、上一页、下一页
     * @param agent         当前代理商
     * @param pageNo        第几页
     * @param pageSize      每页几条
     * @param keywords      关键词
     * @param searchSort    搜索类型
     * @return              班级集合
     */
    public Page<ClassTeam> findClassArrangeExam(Agent agent,Integer pageNo,Integer pageSize,String keywords,String searchSort){
        return  classTeamRepository.findAll(new Specification<ClassTeam>() {
            @Override
            public Predicate toPredicate(Root<ClassTeam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Date date = new Date();
                if ("".equals(keywords)||keywords==null) {
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent)
                    );
                }else if("all".equals(searchSort)){
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.or(
                                    cb.like(root.get("className").as(String.class),"%"+keywords+"%"),
                                    cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%"),
                                    cb.like(root.get("exam").get("examDate").as(String.class), "%" + keywords + "%"),
                                    cb.like(root.get("exam").get("examAddress").as(String.class), "%" + keywords + "%")
                            )
                    );
                }else{
                    if("area".equals(searchSort)) {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%")
                        );
                    }else if("examDate".equals(searchSort)) {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.like(root.get("exam").get("examDate").as(String.class), "%" + keywords + "%")
                        );
                    }else if("examAddress".equals(searchSort)) {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.like(root.get("exam").get("examAddress").as(String.class), "%" + keywords + "%")
                        );
                    } else{
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.like(root.get(searchSort).as(String.class), "%" + keywords + "%")
                        );
                    }
                }
            }
        }, new PageRequest(pageNo, pageSize));
    }

    /**
     * Created by jiashubing on 2015/7/31.
     * 设置学员是否通过考试
     * @param id  学员id
     * @param x   1为通过，2为不通过
     */
    @Transactional
    public void setExamPassById(Long id,int x){
        Member member=memberRepository.findOne(id);
        member.setPassed(x);
        memberRepository.save(member);
    }

    /**
     *  Created by jiashubing on 2015/7/27.
     *  多个学员确认通过考试
     * @param arrayList 学员id集合
     */
    @Transactional
    public void allMemberPassExam(ArrayList<Long> arrayList){
        Member mb = null;
        Date d = new Date();
        for(int i=0; i<arrayList.size();i++){
            mb = memberRepository.findOne(arrayList.get(i));
            mb.setPassed(1);
            memberRepository.save(mb);
        }
    }


    /**
     * Create by shiliting on 2015,8,5
     * 查询代理商
     * @param pageNo    显示第几页代理商
     * @param pageSize  每页显示代理商数
     * @param keyword   查询代理商关键字
     * @param type      查询代理商类型
     * @return
     */
    public Page<Agent> searchAgent(int pageNo,int pageSize,String keyword,String type){
        return  agentRepository.findAll(new Specification<Agent>() {
            @Override
            public Predicate toPredicate(Root<Agent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if ("".equals(keyword)||keyword==null) {
                    return cb.isTrue(root.get("enabled").as(Boolean.class));
                }else if("all".equals(type)){
                    return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)),cb.or(
                            cb.like(root.get("name").as(String.class), "%" + keyword + "%"),
                            cb.like(root.get("phoneNo").as(String.class), "%" + keyword + "%"),
                            cb.like(root.get("area").as(String.class), "%" + keyword + "%"),
                            cb.like(root.get("level").as(String.class), "%" + keyword + "%"),
                            cb.like(root.get("loginName").as(String.class), "%" + keyword + "%")

                            ));

                }else{
                    return  cb.and(
                            cb.like(root.get(type).as(String.class),"%"+keyword+"%"),
                            cb.isTrue(root.get("enabled").as(Boolean.class))
                    );

                }
            }
        },new PageRequest(pageNo, pageSize));
    }

    /**
     * 修改代理商
     * @param agent   修改过的代理商
     */
    @Transactional
    public void modifyAgent(Agent agent){
        agentRepository.save(agent);
    }

    /**
     * 查找一个代理商
     * @param id    代理商ID
     * @return
     */
    public Agent findAgentById(long id){
        return agentRepository.findOne(id);
    }


    /**
     *
     * @param areaId
     * @return
     */
    public boolean checkAreaIdAvailable(String areaId) {
        Agent agent = agentRepository.findByAreaId(areaId);
        if(agent==null) {
            return true;
        }
        return false;
    }

    public List<Agent> findAvailableAgents() {
        return agentRepository.findAll();
    }
}
