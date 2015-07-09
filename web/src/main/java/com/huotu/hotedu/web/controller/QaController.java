package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by shiliting on 2015/6/10.
 * 常见问题有关的Controller
 * @author shiliting shiliting741@163.com
 */
@Controller
public class QaController {
    @Autowired
    private QaService qaService;
   // @PostAuthorize("hasRole('EDITOR')")

    public static final int PAGE_SIZE=10;//每张页面的记录数
    //后台单击常见问题链接显示的消息
    @RequestMapping("/backend/loadQa")
    public String loadQa(Model model){
        Page<Qa> pages=qaService.loadQa(0,PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allQaList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/qa";
    }

    //后台单机搜索按钮显示的常见问题消息
    @RequestMapping("/backend/searchQa")
    public String searchQa(String keywords,Model model){
        Page<Qa> pages=qaService.searchQa(0,PAGE_SIZE,keywords);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allQaList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/qa";
    }

    //后台单击常见问题的分页
    @RequestMapping("/backend/pageQa")
    public String pageQa(int n,int sumpage,String keywords,Model model){
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Qa> pages = qaService.searchQa(n, PAGE_SIZE, keywords);
        model.addAttribute("allQaList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/qa";
    }

    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/delQa")
    public String delQa(int n,int sumpage,String keywords,Long id,Long sumElement,Model model){
        qaService.delQa(id);
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;

        }
        sumElement--;
        Page<Qa> pages = qaService.searchQa(n, PAGE_SIZE, keywords);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allQaList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/qa";
    }




    //后台单击新建按钮
    @RequestMapping("/backend/addQa")
    public String addQa(Model model){
        return "/backend/newqa";
    }
    //后台单机修改按钮
    @RequestMapping("/backend/modifyQa")
    public String modifyQa(Long id, Model model){
        Qa qa=qaService.findOneById(id);
        model.addAttribute("qa",qa);
        return "/backend/modifyqa";
    }


    //后台单击添加保存按钮
    @RequestMapping("/backend/addSaveQa")
    public String addSaveQa(String title,String content,String top,Model model){
        Qa qa=new Qa();
        qa.setTitle(title);
        qa.setContent(content);
        qa.setLastUploadDate(new Date());
        qa.setTop("1".equals(top)? true:false);
        qaService.addQa(qa);
        return "redirect:/backend/loadQa";
    }


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifySaveQa")
    public String modifySaveQa(Long id,String title,String content,Boolean top,Model model){
        Qa qa=qaService.findOneById(id);
        qa.setTitle(title);
        qa.setContent(content);
        qa.setTop(top);
        qa.setLastUploadDate(new Date());
        qaService.modify(qa);
        return "redirect:/backend/loadQa";
    }



}
