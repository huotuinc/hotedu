package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.service.ExamGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by shiliting on 2015/6/10.
 * 考试指南的Controller
 *
 * @author shiliting741@163.com
 */
@Controller
public class ExamGuideController {
    /**
     * 考试指南的service层
     */
    @Autowired
    private ExamGuideService examGuideService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE = 10;

//    /**
//     * 显示考试指南信息
//     *
//     * @param model 返回客户端集
//     * @return examguide.html
//     */
//    @RequestMapping("/backend/loadExamGuide")
//    public String loadExamGuide(Model model) {
//
//        Page<ExamGuide> pages = examGuideService.loadExamGuide(0, PAGE_SIZE);
//        long sumElement = pages.getTotalElements();
//        model.addAttribute("allGuideList", pages);
//        model.addAttribute("sumpage", (sumElement + pages.getSize() - 1) / pages.getSize());
//        model.addAttribute("n", 0);
//        model.addAttribute("keywords", "");
//        model.addAttribute("sumElement", sumElement);
//        return "/backend/examguide";
//    }

    /**
     * 搜索符合条件的考试指南信息
     *
     * @param keywords 搜索关键字 可以为空
     * @param model    返回客户端参数集
     * @return examguide.html
     */
    @RequestMapping("/backend/searchExamGuide")
    public String searchExamGuide(@RequestParam(required = false) String keywords, Model model) {
        Page<ExamGuide> pages;
        if (keywords == null)
            pages = examGuideService.loadExamGuide(0, PAGE_SIZE);
        else
            pages = examGuideService.searchExamGuide(0, PAGE_SIZE, keywords);
        long sumElement = pages.getTotalElements();
        model.addAttribute("allGuideList", pages);
        model.addAttribute("sumpage", (sumElement + pages.getSize() - 1) / pages.getSize());
        model.addAttribute("n", 0);
        model.addAttribute("keywords", keywords);
        model.addAttribute("sumElement", sumElement);
        return "/backend/examguide";
    }

//    /**
//     * 分页显示
//     * @param n             显示第几页
//     * @param sumpage    分页总页数
//     * @param keywords      检索关键字(使用检索功能后有效)
//     * @param model         返回客户端集合
//     * @return          examguide.html
//     * @deprecated 移除
//     */
//    @RequestMapping("/backend/pageExamGuide")
//    public String pageExamGuide(int n,int sumpage,String keywords,Model model){
//        //如果已经到分页的第一页了，将页数设置为0
//        if (n < 0){
//            n++;
//        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
//            n--;
//        }
//        Page<ExamGuide> pages = examGuideService.searchExamGuide(n, PAGE_SIZE, keywords);
//        model.addAttribute("allGuideList",pages);
//        model.addAttribute("sumpage",sumpage);
//        model.addAttribute("n",n);
//        model.addAttribute("keywords",keywords);
//        model.addAttribute("sumElement",pages.getTotalElements());
//        return "/backend/examguide";
//    }

    /**
     * 删除考试指南信息以及查询
     *
     * @param n          显示第几页
     * @param sumpage    分页总页数
     * @param keywords   检索关键字(使用检索功能后有效)
     * @param id         需要被删除的记录id
     * @param sumElement 总记录数
     * @param model      返回客户端集合
     * @return examguide.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delExamGuide")
    public String delExamGuide(int n, int sumpage, String keywords, Long id, Long sumElement, Model model) {
        examGuideService.delExamGuide(id);
        if ((sumElement - 1) % PAGE_SIZE == 0) {
            if (n > 0 && n + 1 == sumpage) {
                n--;
            }
            sumpage--;
        }
        sumElement--;
        Page<ExamGuide> pages = examGuideService.searchExamGuide(n, PAGE_SIZE, keywords);
        model.addAttribute("sumpage", sumpage);
        model.addAttribute("allGuideList", pages);
        model.addAttribute("n", n);
        model.addAttribute("keywords", keywords);
        model.addAttribute("sumElement", sumElement);
        return "/backend/examguide";
    }

    /**
     * examguide.html页面单击新建跳转
     *
     * @return newguide.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addExamGuide")
    public String addExamGuide() {
        return "/backend/newguide";
    }

    /**
     * examguide.html页面点击修改后跳转
     *
     * @param id    需要修改的id
     * @param model 返回客户端集
     * @return modifyguide.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyExamGuide")
    public String modifyExamGuide(Long id, Model model) {
        ExamGuide examGuide = examGuideService.findOneById(id);
        model.addAttribute("examGuide", examGuide);
        return "/backend/modifyguide";
    }

    /**
     * newguide.html页面点击保存添加后跳转
     *
     * @param title   标题
     * @param content 描述
     * @param top     是否置顶
     * @return 不出异常重定向：/backend/loadExamGuide
     */
    //TODO 是否搞抛出异常
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addSaveExamGuide")
    public String addSaveExamGuide(String title, String content, String top) {
        ExamGuide examGuide = new ExamGuide();
        examGuide.setTitle(title);
        examGuide.setContent(content);
        examGuide.setLastUploadDate(new Date());
        examGuide.setIsTop("1".equals(top));
        examGuideService.addExamGuide(examGuide);
        return "redirect:/backend/loadExamGuide";
    }

    /**
     * modifyguide.html页面点击保存修改后跳转
     *
     * @param id      修改后的id
     * @param title   标题
     * @param content 描述
     * @param top     是否置顶
     * @return 重定向到：/backend/loadExamGuide
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveExamGuide")
    public String modifySaveExamGuide(Long id, String title, String content, String top) {
        ExamGuide examGuide = examGuideService.findOneById(id);
        examGuide.setTitle(title);
        examGuide.setContent(content);
        examGuide.setIsTop("1".equals(top));
        examGuide.setLastUploadDate(new Date());
        examGuideService.modify(examGuide);
        return "redirect:/backend/loadExamGuide";
    }
}
