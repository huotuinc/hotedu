package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.service.ExamGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public static final int PAGE_SIZE=10;//每张页面的记录数
    //后台单击考试指南链接显示的消息
    @RequestMapping("/backend/load/examGuide")
    public String loadExamGuide(Model model){
        Page<ExamGuide> pages=examGuideService.loadExamGuide(0,PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/guides";
    }

    //后台单机搜索按钮显示的考试指南消息
    @RequestMapping("/backend/search/examGuide")
    public String searchExamGuide(String keywords,Model model){
        Page<ExamGuide> pages=examGuideService.searchExamGuide(0,PAGE_SIZE,keywords);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/guides";
    }

    //后台单击考试指南的分页
    @RequestMapping("/backend/page/examGuide")
    public String pageExamGuide(int n,int sumpage,String keywords,Model model){
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<ExamGuide> pages = examGuideService.searchExamGuide(n, PAGE_SIZE, keywords);
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/guides";
    }

    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/del/examGuide")
    public String DelExamGuide(int n,int sumpage,String keywords,Long id,Long sumElement,Model model){
        examGuideService.delExamGuide(id);
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;

        }
        sumElement--;
        Page<ExamGuide> pages = examGuideService.searchExamGuide(n, PAGE_SIZE, keywords);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allGuideList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/guides";
    }



}
