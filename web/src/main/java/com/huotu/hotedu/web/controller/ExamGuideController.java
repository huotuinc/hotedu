package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.service.ExamGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by shiliting on 2015/6/10.
 * 考试指南的Controller
 * @author shiliting741@163.com
 */
@Controller
public class ExamGuideController {
    @Autowired
    private ExamGuideService examGuideService;
    public static final int PAGE_SIZE=10;//每张页面的记录数

    //后台单击考试指南链接显示的消息
    @RequestMapping("/backend/loadExamGuide")
    public String loadExamGuide(Model model){
        Page<ExamGuide> pages=examGuideService.loadExamGuide(0,PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",(sumElement+pages.getSize()-1)/pages.getSize());
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/examguide";
    }

    //后台单机搜索按钮显示的考试指南消息
    @RequestMapping("/backend/searchExamGuide")
    public String searchExamGuide(String keywords,Model model){
        Page<ExamGuide> pages=examGuideService.searchExamGuide(0,PAGE_SIZE,keywords);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allGuideList",pages);
        model.addAttribute("sumpage",(sumElement+pages.getSize()-1)/pages.getSize());
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/examguide";
    }

    //后台单击考试指南的分页
    @RequestMapping("/backend/pageExamGuide")
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
        return "/backend/examguide";
    }

    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/delExamGuide")
    public String delExamGuide(int n,int sumpage,String keywords,Long id,Long sumElement,Model model){
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
        return "/backend/examguide";
    }




    //后台单击新建按钮
    @RequestMapping("/backend/addExamGuide")
    public String addExamGuide(Model model){
        return "/backend/newguide";
    }

    //后台单机修改按钮
    @RequestMapping("/backend/modifyExamGuide")
    public String ModifyExamGuide(Long id, Model model){
        ExamGuide examGuide=examGuideService.findOneById(id);
        model.addAttribute("examGuide",examGuide);
        return "/backend/modifyguide";
    }


    //后台单击添加保存按钮
    @RequestMapping("/backend/addSaveExamGuide")
    public String addSaveExamGuide(String title,String content,String top,Model model){
        ExamGuide examGuide=new ExamGuide();
        examGuide.setTitle(title);
        examGuide.setContent(content);
        examGuide.setLastUploadDate(new Date());
        examGuide.setTop("1".equals(top)? true:false);
        examGuideService.addExamGuide(examGuide);
        return "redirect:/backend/loadExamGuide";
    }


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifySaveExamGuide")
    public String modifySaveExamGuide(Long id,String title,String content,Boolean top,Model model){
        ExamGuide examGuide=examGuideService.findOneById(id);
        examGuide.setTitle(title);
        examGuide.setContent(content);
        examGuide.setTop(top);
        examGuide.setLastUploadDate(new Date());
        examGuideService.modify(examGuide);
        return "redirect:/backend/loadExamGuide";
    }
}
