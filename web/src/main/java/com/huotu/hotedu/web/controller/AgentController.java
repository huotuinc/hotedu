package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luffy on 2015/6/10.
 * 登录有关的Controller
 *
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
    public static final int PAGE_SIZE = 10;


    /**
     * Created by jiashubing on 2015/7/24.
     * 将学员保存到新建班级中
     * @param className             新建班级的名字
     * @param noClassMemberArrayLis 复选框选中成员的id集合,Strring类型
     * @param model                 返回客户端集
     * @return                      新建班级页面
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/pc/addSaveNewClassTeam")
    public String addSaveNewClassTeam(@AuthenticationPrincipal Agent agent, String className, String noClassMemberArrayLis, Model model) throws UnsupportedEncodingException {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "redirect:pc/loadClassMembers";
        noClassMemberArrayLis = URLDecoder.decode(noClassMemberArrayLis, "UTF-8");
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(noClassMemberArrayLis);
        if (arrayList == null || arrayList.isEmpty()) {
            errInfo = "成员集合为空，没有需要安排分班的学员";
        } else if (className.equals("")) {
            errInfo = "班级名称为空，请输入新建班级名称";
        } else if (!agentService.checkClassTeamByName(className)) {
            errInfo = "班级名称已经占用，请输入其他名称";
        } else {
            ClassTeam classTeam = new ClassTeam();
            classTeam.setClassName(className);
            classTeam.setAgent(agent);
            ClassTeam ct = agentService.addClassTeam(agent,classTeam);
            agentService.arrangeClass(arrayList, ct);
        }
        model.addAttribute("errInfo", errInfo);
        model.addAttribute("msgInfo", msgInfo);
        return turnPage;
    }

    @RequestMapping("/pc/addSaveNewExam")
    @ResponseBody
    public Result addSaveNewExam(@AuthenticationPrincipal Agent agent,@DateTimeFormat(pattern = "yyyy-MM-dd")Date examDate,String examAddress, String classExamArrayLis) {
        Result result = new Result();
        String examName = examAddress + new SimpleDateFormat("yyyy-MM-dd").format(examDate);
        if(agentService.isExamNameAvailable(examName)){
            MyJsonUtil myJsonUtil = new MyJsonUtil();
            ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(classExamArrayLis);
            Exam exam = new Exam();
            exam.setExamAddress(examAddress);
            exam.setExamDate(examDate);
            exam.setExamName(examName);
            exam.setAgent(agent);
            Exam ex= agentService.addExam(exam);
            agentService.arrangeExam(arrayList, ex);
            result.setStatus(1);
            result.setMessage("安排成功");

        }else {
            result.setStatus(0);
            result.setMessage("考场已经存在，请重新设置考试时间和地点");
        }
        return result;
    }

    /**
     * Created by cwb on 2015/7/24
     * 将学员保存到已有班级中 AJAX实现
     * @param noClassMemberArrayLis 未分班的学员
     * @return
     */
    @RequestMapping("/pc/addMembersIntoExitClass")
    @ResponseBody
    public Result addMembersIntoExitClass(String className,String noClassMemberArrayLis) {
        Result result = new Result();
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(noClassMemberArrayLis);
        if (arrayList == null || arrayList.isEmpty()) {
            result.setStatus(0);
            result.setMessage("成员集合为空，没有需要安排分班的学员");
        } else {
            ClassTeam classTeam = agentService.findClassTeamById(Long.parseLong(className));
            agentService.arrangeClass(arrayList, classTeam);
            result.setStatus(1);
            result.setMessage("分班成功！");
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 将学员保存到已有班级中
     * @param className  已有班级的id
     * @param noClassMemberArrayLis 复选框选中成员的id集合,Strring类型
     * @param model    返回客户端集
     * @return 新建班级页面
     */
    @RequestMapping("/pc/addSaveExistClassTeam")
    public String addSaveOldClassTeam(String className, String noClassMemberArrayLis, Model model) {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "redirect:/pc/loadClassMembers";
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(noClassMemberArrayLis);
        if (arrayList == null || arrayList.isEmpty()) {
            errInfo = "成员集合为空，没有需要安排分班的学员";
        } else {
            ClassTeam classTeam = agentService.findClassTeamById(Long.parseLong(className));
            agentService.arrangeClass(arrayList, classTeam);
        }
        model.addAttribute("errInfo", errInfo);
        model.addAttribute("msgInfo", msgInfo);
        return turnPage;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 显示未分班的学员信息
     * 加载、搜索、上一页、下一页
     * @param agent      当前代理商
     * @param keywords   关键词
     * @param searchSort 搜索类型
     * @param pageNo     第几页
     * @param noClassMemberArrageNewClassDivStyle    新建班级框是否显示
     * @param model      返回客户端集
     * @return yun-daili.html  班级管理选项卡
     */
    @RequestMapping("/pc/loadClassMembers")
    public String loadClassMembers(@AuthenticationPrincipal Agent agent,
                                     @RequestParam(required = false) String keywords,
                                     @RequestParam(required = false) String searchSort,
                                     @RequestParam(required = false) Integer pageNo,
                                     @RequestParam(required = false) Boolean noClassMemberArrageNewClassDivStyle,
                                     Model model) {
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Member> pages = agentService.findNoClassMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        if (pages.getNumberOfElements() == 0) {
            pageNo = pages.getTotalPages() - 1;
            pageNo = pageNo<0 ? 0 : pageNo;
            pages = agentService.findNoClassMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        }
        model.addAttribute("agent", agent);
        model.addAttribute("allClassMembersList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "bjgl");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("noClassMemberArrageNewClassDivStyle", noClassMemberArrageNewClassDivStyle);
        model.addAttribute("existClassDivStyle",false);
        model.addAttribute("noClassMemberArrageClassDivStyle",false);

        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 判断新建班级的名称是否已经存在
     * @param className             新建班级的名字
     * @param noClassMemberArrayLis 复选框选中成员的id集合,Strring类型
     * @param model                 返回客户端集
     * @return 重定向到addSaveNewClassTeam 将成员添加保存到新建班级
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/pc/isClassNameExist")
    public String isClassNameExist(String className, String noClassMemberArrayLis, Model model) throws UnsupportedEncodingException {
        String errInfo = "";
        Boolean style = false;
        String turnPage = "";
        boolean flag = agentService.checkClassTeamByName(className);
        if (flag) {
            turnPage = "redirect:/pc/addSaveNewClassTeam";
        } else {
            errInfo = "该班级名字已经被注册,请使用其他的名字";
            style = true;
            turnPage = "redirect:/pc/loadClassMembers";
        }
        model.addAttribute("noClassMemberArrageNewClassDivStyle", style);
        model.addAttribute("className", className);
        model.addAttribute("noClassMemberArrayLis", URLEncoder.encode(noClassMemberArrayLis, "UTF-8"));
        model.addAttribute("errInfo", errInfo);
        return turnPage;
    }

    /**
     * Created by cwb on 2015/7/29
     * 当前代理商选择已有班级 AJAX实现
     * @param agent 当前代理商
     * @return  AJAX实现已有班级
     */
    @RequestMapping("/pc/loadAvailableClassTeams")
    @ResponseBody
    public Result loadAvailableClassTeams(@AuthenticationPrincipal Agent agent) {
        Result result = new Result();
        List<ClassTeam> existClassList = agentService.findAvailableClassTeams(agent);
        if(existClassList==null) {
            result.setStatus(0);
            result.setMessage("没有可用的班级，请新建");
        }else {
            result.setStatus(1);
            result.setBody(existClassList);
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/30.
     * 当前代理商选择已有的考场
     * @param agent 当前代理商
     * @return  AJAX实现已有考场
     */
    @RequestMapping("/pc/loadAvailableExamTeams")
    @ResponseBody
    public Result loadAvailableExamTeams(@AuthenticationPrincipal Agent agent) {
        Result result = new Result();
        List<Exam> existExamList = agentService.findAvailableExamTeams(agent);
        if(existExamList==null) {
            result.setStatus(0);
            result.setMessage("没有可用的考场，请新建");
        }else {
            result.setStatus(1);
            result.setBody(existExamList);
        }
        return result;
    }

//    /**
//     * Created by jiashubing on 2015/7/28.
//     * 查找当前代理商已有班级
//     * @param agent     当前代理商
//     * @param model     返回客户端集
//     * @return
//     */
//    @RequestMapping("/pc/findExistClassAll")
//    public String findExistClassAll(@AuthenticationPrincipal Agent agent,
//                                    @RequestParam(required = false) String keywords,
//                                    @RequestParam(required = false) String searchSort,
//                                    String noClassMemberArrayLis,
//                                    @RequestParam(required = false) Integer pageNo,
//                                    Model model){
//        String turnPage = "/pc/yun-daili";
//        Page<Member> pages = agentService.findNoClassMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
//        long totalRecords = pages.getTotalElements();
//        model.addAttribute("agent", agent);
//        model.addAttribute("allNoClassMembersList", pages);
//        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
//        model.addAttribute("navigation", "bjgl");
//        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
//        model.addAttribute("keywords", keywords);
//        model.addAttribute("pageNo", pageNo);
//        model.addAttribute("totalRecords", totalRecords);
//        model.addAttribute("totalPages", pages.getTotalPages());
//
//        List<ClassTeam> list = agentService.findExistClassAll(agent);
//        model.addAttribute("noClassMemberArrayLis",noClassMemberArrayLis);
//        model.addAttribute("existClassList",list);
//        model.addAttribute("existClassDivStyle",true);
//        model.addAttribute("noClassMemberArrageClassDivStyle",false);
//        return turnPage;
//    }

    /**
     * Created by jiashubing on 2015/7/30.
     * 显示毕业管理员信息
     * 加载、搜索、上一页、下一页
     * @param agent      当前代理商
     * @param keywords   关键词
     * @param searchSort 搜索类型
     * @param pageNo     第几页
     * @param model      返回客户端集
     * @return           yun-daili.html  毕业管理选项卡
     */
    @RequestMapping("/pc/loadClassExam")
    public String loadClassExam(@AuthenticationPrincipal Agent agent,
                                   @RequestParam(required = false) String keywords,
                                   @RequestParam(required = false) String searchSort,
                                   @RequestParam(required = false) Integer pageNo,
                                   @RequestParam(required = false) Boolean noClassMemberArrageNewClassDivStyle,
                                   Model model) {
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<ClassTeam> pages = agentService.findClassArrageExam(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        if (pages.getNumberOfElements() == 0) {
            pageNo = pages.getTotalPages() - 1;
            pageNo = pageNo<0 ? 0 : pageNo;
            pages = agentService.findClassArrageExam(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        }
        model.addAttribute("agent", agent);
        model.addAttribute("allClassExamList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "apkc");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("noClassMemberArrageNewClassDivStyle", noClassMemberArrageNewClassDivStyle);
        model.addAttribute("existClassDivStyle",false);
        model.addAttribute("classArrageExamDiv",false);

        return "/pc/yun-daili";
    }


///**
// *   ckm
// *显示已经考试过的学员
// *
// */
// @RequestMapping("/pc/loadExamedMembersAll")
//    public String loadExamedMembers (@AuthenticationPrincipal Agent agent,
//                                     @RequestParam(required = false) String keywords,
//                                     @RequestParam(required = false) String searchSort,
//                                     @RequestParam(required = false) Integer pageNo,
//                                     @RequestParam(required = false) Boolean ExamedMemberArrageClassDiv3Style,
//                                     @RequestParam(required = false) String isGraduated,
//                                     Model model){
//    if (pageNo == null || pageNo < 0) {
//        pageNo = 0;
//    }
//    if(isGraduated==null||"".equals(isGraduated)) {
//        isGraduated = "false";
//    }
//    Page<Member> pages = "true".equals(isGraduated) ? agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort) : agentService.findNoClassMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
//    long totalRecords = pages.getTotalElements();
//    if (pages.getNumberOfElements() == 0) {
//        pageNo = pages.getTotalPages() - 1;
//        pageNo = pageNo<0 ? 0 : pageNo;
//        pages = "true".equals(isGraduated) ? agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort) : agentService.findNoClassMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
//    }
//    model.addAttribute("classAndExam","true".equals(isGraduated)?"kssj":"apbj");
//    model.addAttribute("agent", agent);
//    model.addAttribute("allClassMembersList", pages);
//    model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
//    model.addAttribute("navigation", "bygl");
//    model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
//    model.addAttribute("keywords", keywords);
//    model.addAttribute("pageNo", pageNo);
//    model.addAttribute("totalRecords", totalRecords);
//    model.addAttribute("totalPages", pages.getTotalPages());
//    model.addAttribute("ExamedMemberArrageClassDiv3Style", ExamedMemberArrageClassDiv3Style);
//    model.addAttribute("isGraduated", isGraduated);
//    model.addAttribute("existClassDivStyle",false);
//    model.addAttribute("noPassedMemberArrageClassDivStyle",false);
//
//        return "/pc/yun-daili";
//     }


    @RequestMapping("/pc/loadGraduationMembers")
    public String loadGraduationMembers(@AuthenticationPrincipal Agent agent,
                                        @RequestParam(required = false) String keywords,
                                        @RequestParam(required = false) String searchSort,
                                        @RequestParam(required = false) Integer pageNo,
                                        Model model){
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Member> pages = agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        if (pages.getNumberOfElements() == 0) {
            pageNo = pages.getTotalPages() - 1;
            pageNo = pageNo<0 ? 0 : pageNo;
            pages = agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        }
        model.addAttribute("agent", agent);
        model.addAttribute("allExamMembersList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "bygl");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());

        return "/pc/yun-daili";
    }


}
