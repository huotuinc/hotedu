package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.ClassTeamService;
import com.huotu.hotedu.service.MemberService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by shiliting on 2015/8/7.
 * 班级有关的Controller
 * @author shiliting
 */
@Controller
public class ClassTeamController {
    //进入班级管理界面

    @Autowired
    private ClassTeamService classTeamService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private StaticResourceService staticResourceService;
    @Autowired
    private MemberService memberService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/lookAgentClass")
    public String searchClassTeam(@RequestParam(required = false)Integer pageNo,
                                  @RequestParam(required = false) String keywords,
                                  long id, Model model) throws Exception{
        String turnPage="/backend/agent";
        Agent agent=agentService.findOneById(id);
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<ClassTeam> pages = classTeamService.searchClassTeam(agent, pageNo, PAGE_SIZE, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = classTeamService.searchClassTeam(agent,pageNo, PAGE_SIZE,keywords);
            totalRecords = pages.getTotalElements();
        }

        if(agent.getPictureUri()!=null){
            agent.setPictureUri(staticResourceService.getResource(agent.getPictureUri()).toURL().toString());
        }
        model.addAttribute("totalMembers",memberService.searchMembers(agent,0,PAGE_SIZE).getTotalElements());
        model.addAttribute("agent",agent);
        model.addAttribute("allAgentClassList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }



    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/agentClassMember")
    public String agentClassMember(@RequestParam(required = false)Integer pageNo,
                                   @RequestParam(required = false) String keywords,
                                   @RequestParam(required = false,value = "searchSort")String searchSort,
                                   Long classId,Model model) throws Exception{
        String turnPage="/backend/batch";
        ClassTeam classTeam=classTeamService.findOneById(classId);
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Member> pages = memberService.searchMembersByClass(pageNo, PAGE_SIZE, keywords, classTeam, searchSort);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = memberService.searchMembersByClass(pageNo, PAGE_SIZE, keywords,classTeam,searchSort);
            totalRecords = pages.getTotalElements();
        }
        model.addAttribute("classTeam",classTeam);
        model.addAttribute("allMemberList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort",searchSort==null?"all":searchSort);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }




}

