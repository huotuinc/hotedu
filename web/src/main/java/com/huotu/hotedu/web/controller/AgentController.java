package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

/**
 * Created by luffy on 2015/6/10.
 * 登录有关的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class AgentController {

    @Autowired
    private AgentService agentService;
    @Autowired
    private MemberService memberService;


    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    /**
     * Created by jiashubing on 2015/7/24.
     * 将学员保存到新建班级中
     * @param className     新建班级的名字
     * @param arrayLis      复选框选中成员的id集合,Strring类型
     * @param model         返回客户端集
     * @return              新建班级页面
     */
    @RequestMapping("pc/addSaveNewClassTeam")
    public String addSaveNewClassTeam(String className,String arrayLis,Model model){
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/agentCenter";
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Object> arrayList = myJsonUtil.convertJsonBytesToArrayList(arrayLis);
        if(arrayList==null || arrayList.isEmpty()){
            errInfo = "成员集合为空，没有需要安排分班的学员";
        } else if(className.equals("")){
            errInfo = "班级名称为空，请输入新建班级名称";
        } else if(!agentService.checkClassTeamByName(className)) {
            errInfo = "班级名称已经占用，请输入其他名称";
        } else {
            ClassTeam classTeam = new ClassTeam();
            classTeam.setClassName(className);
            ClassTeam ct = agentService.addClassTeam(classTeam);
            agentService.arrangeClass(arrayList,ct);
        }
        model.addAttribute("errInfo",errInfo);
        model.addAttribute("msgInfo",msgInfo);
        return turnPage;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 将学员保存到已有班级中
     * @param classId       已有班级的名字
     * @param arrayLis      复选框选中成员的id集合,Strring类型
     * @param model         返回客户端集
     * @return              新建班级页面
     */
    @RequestMapping("pc/addSaveOldClassTeam")
    public String addSaveOldClassTeam(Long classId,String arrayLis,Model model){
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/agentCenter";
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Object> arrayList = myJsonUtil.convertJsonBytesToArrayList(arrayLis);
        if(arrayList==null || arrayList.isEmpty()){
            errInfo = "成员集合为空，没有需要安排分班的学员";
        } else {
            ClassTeam classTeam = agentService.findOneClassTeamById(classId);
            agentService.arrangeClass(arrayList,classTeam);
        }
        model.addAttribute("errInfo",errInfo);
        model.addAttribute("msgInfo",msgInfo);
        return turnPage;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 显示未分班的学员信息
     * 加载、搜索、上一页、下一页
     * @param agent         当前代理商
     * @param keywords      关键词
     * @param searchSort    搜索类型
     * @param pageNo        第几页
     * @param model         返回客户端集
     * @return              yun-daili.html  班级管理选项卡
     */
    @RequestMapping("pc/loadNoClassMembers")
    public String loadNoClassMembers(@AuthenticationPrincipal Agent agent,
                                     @RequestParam(required = false)String keywords,
                                     @RequestParam(required = false)String searchSort,
                                     @RequestParam(required = false)Integer pageNo,
                                     Model model){
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Member> pages= agentService.findNoClassMembers(agent,0, 10,keywords,searchSort);
        long totalRecords=pages.getTotalElements();
        if(pages.getNumberOfElements()==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages= agentService.findNoClassMembers(agent,0, 10,keywords,searchSort);
        }
        model.addAttribute("agent",agent);
        model.addAttribute("allNoClassMembersList",pages);
        model.addAttribute("totalMembers",memberService.searchMembers(agent,pageNo,PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation","bjgl");
        model.addAttribute("searchSort",searchSort==null?"all":searchSort);
        model.addAttribute("keywords", keywords==null?"":keywords);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages",totalRecords/pages.getSize()+(totalRecords%pages.getSize()>0? 1:0));

        return "/pc/yun-daili";
    }
}
