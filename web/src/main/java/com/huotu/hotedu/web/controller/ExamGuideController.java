package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.service.ExamGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luffy on 2015/6/10.
 * 考试指南的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class ExamGuideController {
    @Autowired
    private ExamGuideService examGuideService;
    //后台单击考试指南链接显示的消息
    @RequestMapping("/backend/load/examGuide")
    public String loadExamGuide(Model model){
        Page<ExamGuide> pages=examGuideService.loadExamGuide(0,10);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("pageSize",10);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/guides";
    }


    @RequestMapping("/backend/search/examGuide")
    public String searchExamGuide(String keywords,Model model){
        Page<ExamGuide> pages=examGuideService.searchExamGuide(0,10,keywords);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("pageSize",10);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/guides";
    }






    //后台单击考试指南的分页
    @RequestMapping("/backend/page/examGuide")
    public String pageExamGuide(int n,int pageSize,int sumpage,String keywords,Model model){
        //如果已经到分页的第一页了，将页数设置为0
        if(n<0) n++;
        //如果超过分页的最后一页了，将页数设置为最后一页
        if(n+1>sumpage) n--;
        Page<ExamGuide> pages=examGuideService.searchExamGuide(n, pageSize,keywords);
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/guides";
    }


}
