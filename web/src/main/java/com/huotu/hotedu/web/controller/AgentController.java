package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.ClassTeamService;
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

    @Autowired
    private ClassTeamService classTeamService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE = 10;


    /**
     * Created by jiashubing on 2015/7/24.
     * 将学员保存到新建班级中
     * @param agent     当前代理商
     * @param className             新建班级的名字
     * @param noClassMemberArrayLis 复选框选中成员的id集合,Strring类型
     * @return      confirm.js中 check_arrageNewClass方法
     */
    @RequestMapping("/pc/addSaveNewClassTeam")
    @ResponseBody
    public Result addSaveNewClassTeam(@AuthenticationPrincipal Agent agent, String className, String noClassMemberArrayLis) {
        Result result = new Result();
        if(agentService.isClassTeamNameAvailable(className)){
            MyJsonUtil myJsonUtil = new MyJsonUtil();
            ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(noClassMemberArrayLis);
            ClassTeam classTeam = new ClassTeam();
            classTeam.setAgent(agent);
            classTeam.setClassName(className);
            ClassTeam ct = agentService.addClassTeam(classTeam);
            agentService.arrangeClass(arrayList, ct);
            result.setStatus(1);
            result.setMessage("安排成功");

        }else {
            result.setStatus(0);
            result.setMessage("班级已经存在，请重新设置班级名称");
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 将班级保存到新建考场中  AJAX
     * @param agent     当前代理商
     * @param examDate      考试时间
     * @param examAddress       考试地点
     * @param classExamArrayLis     需要安排的班级
     * @return      confirm.js中 check_arrageNewExam方法
     */
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
            Exam ex= agentService.addExam(agent, exam);
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
     * @return      confirm.js中 check_arrageExistClass方法
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
     * Created by jiashubing on 2015/7/30
     * 将班级保存到已有考场中 AJAX实现
     * @param classExamArrayLis 未分考场的班级
     * @return confirm.js中 check_arrageExistExam方法
     */
    @RequestMapping("/pc/addClassIntoExistExam")
    @ResponseBody
    public Result addClassIntoExistExam(String examName,String classExamArrayLis) {
        Result result = new Result();
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(classExamArrayLis);
        if (arrayList == null || arrayList.isEmpty()) {
            result.setStatus(0);
            result.setMessage("班级集合为空，没有需要安排分考场的班级");
        } else {
            Exam exam = agentService.findExamById(Long.parseLong(examName));
            agentService.arrangeExam(arrayList, exam);
            result.setStatus(1);
            result.setMessage("分配考场成功！");
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

        return "/pc/yun-daili";
    }

    /**
     * Created by cwb on 2015/7/29
     * 当前代理商选择已有班级 AJAX实现
     * @param agent 当前代理商
     * @return  indexuser.js中 btn_chooseExistClass按钮事件
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
    @RequestMapping("/pc/loadAvailableExam")
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

    /**
     * Created by jiashubing on 2015/7/30.
     * 显示安排考场信息
     * @param agent      当前代理商
     * @param keywords   关键词
     * @param searchSort 搜索类型
     * @param pageNo     第几页
     * @param model      返回客户端集
     * @return           yun-daili.html  安排考场
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

        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/7/30.
     * 显示毕业管理信息
     * 加载、搜索、上一页、下一页
     * @param agent      当前代理商
     * @param keywords   关键词
     * @param searchSort 搜索类型
     * @param passedSortText 是否通过考试的状态
     * @param pageNo     第几页
     * @param model      返回客户端集
     * @return           yun-daili.html  毕业管理选项卡
     */
    @RequestMapping("/pc/loadGraduationMembers")
    public String loadGraduationMembers(@AuthenticationPrincipal Agent agent,
                                        @RequestParam(required = false) String keywords,
                                        @RequestParam(required = false) String searchSort,
                                        @RequestParam(required = false) Integer passedSortText,
                                        @RequestParam(required = false) Integer pageNo,
                                        Model model){
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Member> pages = agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, passedSortText, searchSort);
        long totalRecords = pages.getTotalElements();
        if (pages.getNumberOfElements() == 0) {
            pageNo = pages.getTotalPages() - 1;
            pageNo = pageNo<0 ? 0 : pageNo;
            pages = agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, passedSortText, searchSort);
        }
        model.addAttribute("agent", agent);
        model.addAttribute("allExamMembersList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "bygl");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("passedSortText", passedSortText == null ? 4 : passedSortText);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());

        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/7/31.
     * 设置学员通过考试
     * @param id  学员id
     * @return  confirm.js中 btn_setExamPass方法
     */
    @RequestMapping("/pc/setExamPass")
    @ResponseBody
    public Result setExamPass(Long id){
        Result result = new Result();
        agentService.setExamPassById(id, 1);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/31.
     * 设置学员通过考试
     * @param id  学员id
     * @return  confirm.js中 btn_setExamNoPass方法
     */
    @RequestMapping("/pc/setExamNoPass")
    @ResponseBody
    public Result setExamNoPass(Long id){
        Result result = new Result();
        agentService.setExamPassById(id, 2);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * Created by jiashubing on 2015/8/1.
     * 班级管理 安排考场 查看班级详细信息
     * @param agent         当前代理商
     * @param keywords      关键词
     * @param searchSort    搜索类型
     * @param pageNo        第几页
     * @param id            查看班级的id
     * @param model         返回客户端集
     * @return       查看班级详细信息
     */
    @RequestMapping("/pc/loadClassTeamDetailInfo")
    public String loadClassTeamDetailInfo(@AuthenticationPrincipal Agent agent,
                                    @RequestParam(required = false) String keywords,
                                    @RequestParam(required = false) String searchSort,
                                    @RequestParam(required = false) Integer pageNo,
                                    Long id,Model model){
        ClassTeam classTeam = classTeamService.findOneById(id);
        model.addAttribute("classTeamInfo", classTeam);
        model.addAttribute("classTeamDetailcss", true);

        Page<ClassTeam> pages = agentService.findClassArrageExam(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        model.addAttribute("agent", agent);
        model.addAttribute("allClassExamList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "apkc");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());
        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/8/1.
     * 修改班级名称 AJAX
     * @param id        班级ID
     * @param className     修改的名称
     * @return      confirm.js中 btn_modifyClassTeamInfo方法
     */
    @RequestMapping("/pc/modifyClassTeamName")
    @ResponseBody
    public Result modifyClassTeamName(Long id,String className) {
        Result result = new Result();
        agentService.modifyClassTeamName(id, className);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * Created by jiashubing on 2015/8/3.
     * 毕业管理的确认通过
     * @param checkExamMemberList  学员id集合
     * @return  confirm.js中 check_ExamMemberEnter方法
     */
    @RequestMapping("/pc/allMemberPassExam")
    @ResponseBody
    public Result allMemberPassExam(String checkExamMemberList){
        Result result = new Result();
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(checkExamMemberList);
        agentService.allMemberPassExam(arrayList);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }

}
