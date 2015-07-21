package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Enterprise;
import com.huotu.hotedu.entity.Exam;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.service.ClassTeamService;
import com.sun.jndi.cosnaming.IiopUrl;
import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
import com.sun.xml.internal.ws.wsdl.writer.document.http.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.util.calendar.LocalGregorianCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luffy on 2015/6/10.
 * 登录有关的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class ClassTeamController {
    //进入班级管理界面

    @Autowired
    private ClassTeamService classTeamService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    @RequestMapping("/backend/loadClassTeam")
    public String loadClassTeam(Model model) {
        Page<ClassTeam> pages = classTeamService.loadClassTeam(0, PAGE_SIZE);
        long sumElement = pages.getTotalElements();
        model.addAttribute("allclassteamList", pages);
        model.addAttribute("sumpage", (sumElement + pages.getSize() - 1) / pages.getSize());
        model.addAttribute("n", 0);
        model.addAttribute("keywords", "");
        model.addAttribute("sumElement", sumElement);
        return "/backend/classteam";
    }
    /**
     * 搜索符合条件的classteam信息
     *
     * @param keywords 搜索关键字 可以为空
     * @param model    返回客户端参数集
     * @return examguide.html
     */
    @RequestMapping("/backend/searchClassTeam")
    public String searchClassTeam(@RequestParam(required = false)Integer pageNo,
                                  @RequestParam(required = false)Integer pageSize,
                                  @RequestParam(required = false) String keywords, Model model) {
        if(pageNo==null){
            pageNo=0;
        }
        if(pageSize==null) {
            pageSize = PAGE_SIZE;
        }
        Page<ClassTeam> pages;
        if (keywords == null) {
            pages = classTeamService.loadClassTeam(pageNo, pageSize);
        }
        else {
            pages = classTeamService.searchClassTeam(pageNo, pageSize, keywords);
        }
        long sumElement = pages.getTotalElements();
        model.addAttribute("allclassteamList", pages);
        model.addAttribute("sumpage", (sumElement + pages.getSize() - 1) / pages.getSize());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("sumElement", sumElement);
        return "/backend/classteam";
    }


    /**
     * classteam.html页面点击修改后跳转
     *
     * @param id    需要修改的id
     * @param model 返回客户端集
     * @return modifyclassteam.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyClassTeam")
    public String modifyClassTeam(Long id, Model model) {
        ClassTeam classTeam = classTeamService.findOneById(id);
        model.addAttribute("classteam",classTeam);
        return "/backend/modifyclassteam";
    }

    /**
     * modifyclassteam.html页面点击保存修改后跳转
     *
     * @param id      修改后的id
     * @param exam    考试信息
     * @return 重定向到：/backend/loadClassTeam
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveClassTeam")
    public String modifySaveClassTeam(Long id, Exam exam) {
        ClassTeam classteam = classTeamService.findOneById(id);
        classteam.setExam(exam);
        classTeamService.modify(classteam);
        return "redirect:/backend/searchClassTeam";
    }

}

