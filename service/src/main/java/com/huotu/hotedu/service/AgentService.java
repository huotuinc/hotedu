package com.huotu.hotedu.service;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Exam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.AgentRepository;
import com.huotu.hotedu.repository.ClassTeamRepository;
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
import java.util.ArrayList;
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
                if ("".equals(keyword)||keyword==null)
                    return cb.isTrue(root.get("enabled").as(Boolean.class));
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)), cb.like(root.get(type).as(String.class), "%" + keyword + "%"));
            }
        }, new PageRequest(n, pageSize));
    }

    /**
     * 分页依据全部搜索 搜索的代理商
     * @param n             第几页
     * @param pageSize      每页显示几条
     * @param keyword       关键词
     * @return              代理商集合
     */
    public Page<Agent> searchAgentAll(int n,int pageSize,String keyword){
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
     * @param agent 代理商对象faks
     */
    public Agent addAgent(Agent agent){return agentRepository.save(agent);}

    /**
     * 修改一位代理商
     * @param agent 代理商对象
     */
    public void modify(Agent agent){agentRepository.save(agent);
    }

    /**
     * 查找一位代理商
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
     *
     * 查找一个已有班级
     * @param id 班级id
     * @return 班级对象
     */
    public ClassTeam findOneClassTeamById(Long id){return classTeamRepository.findOne(id);}

    /**
     * Created by jiashubing on 2015/7/24.
     * 检查该班级名称是否已经使用
     * @param className     班级名称
     * @return              若没有使用，返回true；若使用过，则返回false
     */
    public boolean checkClassTeamByName(String className){
        List<ClassTeam> list = classTeamRepository.findAll(new Specification<ClassTeam>() {
            @Override
            public Predicate toPredicate(Root<ClassTeam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("className").as(String.class), className);
            }
        });
        return list.isEmpty();
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
                if ("".equals(keywords)||keywords==null) {
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.isTrue(root.get("enabled").as(Boolean.class)),
                            cb.isNull(root.get("theClass").as(ClassTeam.class))
                    );
                }else if("all".equals(searchSort)){
                    return cb.and(
                            cb.equal(root.get("agent").as(Agent.class), agent),
                            cb.isTrue(root.get("enabled").as(boolean.class)),
                            cb.isNull(root.get("theClass").as(ClassTeam.class)),
                            cb.or(
                                    cb.like(root.get("realName").as(String.class),"%"+keywords+"%"),
                                    cb.like(root.get("phoneNo").as(String.class),"%"+keywords+"%"),
                                    cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%")
                            )
                    );
                }else{
                    if("area".equals(searchSort)) {
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(boolean.class)),
                                cb.isNull(root.get("theClass").as(ClassTeam.class)),
                                cb.like(root.get("agent").get("area").as(String.class), "%" + keywords + "%")
                        );
                    }
                    else{
                        return cb.and(
                                cb.equal(root.get("agent").as(Agent.class), agent),
                                cb.isTrue(root.get("enabled").as(boolean.class)),
                                cb.isNull(root.get("theClass").as(ClassTeam.class)),
                                cb.like(root.get(searchSort).as(String.class), "%" + keywords + "%")
                        );
                    }
                }
            }
        }, new PageRequest(pageNo, pageSize));
    }
//
//    /**
//     * Created by jiashubing on 2015/7/24.
//     * 显示所有未分班学员 每页10条
//     * @param n         第几页
//     * @param pagesize  每页几条
//     * @return          学员集合
//     */
//    public Page<Member> findNoClassMembers(Integer n,Integer pagesize){
//        return  memberRepository.findAll(new Specification<Member>() {
//            @Override
//            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)), cb.isNull(root.get("theClass").as(ClassTeam.class)));
//            }
//        }, new PageRequest(n, pagesize));
//    }


    /**
     * Created by jiashubing on 2015/7/24.
     * 显示所有已分班学员 每页10条
     * @param n         第几页
     * @param pagesize  每页几条
     * @return          学员集合
     */
    public Page<Member> findHaveClassMembers(Integer n,Integer pagesize){
        return  memberRepository.findAll(new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.isTrue(root.get("enabled").as(Boolean.class)), cb.isNotNull(root.get("theClass").as(ClassTeam.class)));
            }
        }, new PageRequest(n, pagesize));
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 安排分班
     * @param allNoClassMemberList  需要分班的成员
     * @param classTeam             分配的班级
     */
    public void arrangeClass(ArrayList<Object> allNoClassMemberList,ClassTeam classTeam){
        Member mb = null;
        for (Object x : allNoClassMemberList) {
            mb = memberRepository.findOne((long)allNoClassMemberList.get((Integer) x));
            mb.setTheClass(classTeam);
        }
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 查询该代理商已有班级
     * @param agent     所属代理商
     * @return          班级集合
     */
    public List<ClassTeam> findExistClassAll(Agent agent){
        return classTeamRepository.findAll(new Specification<ClassTeam>() {
            @Override
            public Predicate toPredicate(Root<ClassTeam> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.and(cb.equal(root.get("agent").as(Agent.class), agent), cb.isNotNull(root.get("exam").as(Exam.class)));
            }
        });
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 注册增加班级
     * @param classTeam  注册的班级
     */
    public ClassTeam addClassTeam(ClassTeam classTeam){
        return classTeamRepository.save(classTeam);
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 代理商添加班级
     * @param agent         代理商
     * @param classTeam     添加的班级
     */
    public void agentAddClassTeam(Agent agent,ClassTeam classTeam){
        classTeam.setAgent(agent);
        classTeamRepository.save(classTeam);
    }

}
