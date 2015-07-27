package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.MemberService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
     * @param className                     新建班级的名字
     * @param noClassMemberArrayLis         复选框选中成员的id集合,Strring类型
     * @param model                         返回客户端集
     * @return                              新建班级页面
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/pc/addSaveNewClassTeam")
    public String addSaveNewClassTeam(String className,String noClassMemberArrayLis,Model model) throws UnsupportedEncodingException {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "redirect:/pc/loadNoClassMembers";
        noClassMemberArrayLis = URLDecoder.decode(noClassMemberArrayLis,"UTF-8");
        System.out.println("noClassMemberArrayLis = "+noClassMemberArrayLis +"className = "+className);

        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(noClassMemberArrayLis);
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
    @RequestMapping("/pc/addSaveOldClassTeam")
    public String addSaveOldClassTeam(Long classId,String arrayLis,Model model){
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "redirect:/pc/loadNoClassMembers";
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(arrayLis);
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
    @RequestMapping("/pc/loadNoClassMembers")
    public String loadNoClassMembers(@AuthenticationPrincipal Agent agent,
                                     @RequestParam(required = false)String keywords,
                                     @RequestParam(required = false)String searchSort,
                                     @RequestParam(required = false)Integer pageNo,
                                     @RequestParam(required = false)Boolean noClassMemberArrageClassDiv2Style,
                                     Model model){
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Member> pages= agentService.findNoClassMembers(agent,pageNo,PAGE_SIZE,keywords,searchSort);
        long totalRecords=pages.getTotalElements();
        if(pages.getNumberOfElements()==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages= agentService.findNoClassMembers(agent,pageNo, PAGE_SIZE,keywords,searchSort);
        }
        model.addAttribute("agent",agent);
        model.addAttribute("allNoClassMembersList",pages);
        model.addAttribute("totalMembers",memberService.searchMembers(agent,pageNo,PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation","bjgl");
        model.addAttribute("searchSort",searchSort==null?"all":searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo",pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("noClassMemberArrageClassDiv2Style",noClassMemberArrageClassDiv2Style);

        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 判断新建班级的名称是否已经存在
     * @param className                     新建班级的名字
     * @param noClassMemberArrayLis         复选框选中成员的id集合,Strring类型
     * @param model                         返回客户端集
     * @return          重定向到addSaveNewClassTeam 将成员添加保存到新建班级
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/pc/isClassNameExist")
    public String isClassNameExist(String className,String noClassMemberArrayLis,Model model) throws UnsupportedEncodingException {
        String errInfo = "";
        Boolean style= false;
        String turnPage = "";
        System.out.println("noClassMemberArrayLis = "+noClassMemberArrayLis +"className = "+className);
        boolean flag=agentService.checkClassTeamByName(className);
        if(flag) turnPage = "redirect:/pc/addSaveNewClassTeam";
        else{
            errInfo = "该班级名字已经被注册,请使用其他的名字";
            style=true;
            turnPage = "redirect:/pc/loadNoClassMembers";
        }
        model.addAttribute("noClassMemberArrageClassDiv2Style",style);
        model.addAttribute("className",className);
        model.addAttribute("noClassMemberArrayLis", URLEncoder.encode(noClassMemberArrayLis,"UTF-8"));
        model.addAttribute("errInfo",errInfo);
        return  turnPage;
    }
}
